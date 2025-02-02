package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;

import java.util.Date;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // Valeurs de test
    private final String jwtSecret = "testSecret"; // Secret de test
    private final int jwtExpirationMs = 3600000;  // 1 heure

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        // Injection correcte des champs privés
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void shouldGenerateValidJwtToken() {
        // Simuler l'authentification
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "First", "Last", false, "password");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        // Générer le token
        String token = jwtUtils.generateJwtToken(authentication);

        // Vérifications
        assertThat(token).isNotNull();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnFalseForInvalidJwtToken() {
        String invalidToken = "invalid.jwt.token";

        assertThat(jwtUtils.validateJwtToken(invalidToken)).isFalse();
    }

    @Test
    void shouldReturnFalseForExpiredJwtToken() {
        // Générer un token expiré
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // Expiré depuis 2 heures
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // Expiré depuis 1 heure
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
    }


    @Test
    void shouldThrowExceptionForMalformedJwtToken() {
        String malformedToken = "malformed.token";

        // Vérifie que le token mal formé est bien invalide
        assertThat(jwtUtils.validateJwtToken(malformedToken)).isFalse();
    }

    @Test
    void shouldGetUserNameFromJwtToken() {
        // Générer un token
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Extraire le nom d'utilisateur
        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(username).isEqualTo("test@example.com");
    }


    @Test
    void shouldReturnFalseForJwtWithMalformedStructure() {
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.malformedPayload.signature";

        assertThat(jwtUtils.validateJwtToken(malformedToken)).isFalse();
    }

    @Test
    void shouldReturnFalseForJwtWithoutSignature() {
        String unsignedToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .compact();

        assertThat(jwtUtils.validateJwtToken(unsignedToken)).isFalse();
    }

    @Test
    void shouldValidateJwtWithoutExpiration() {
        String tokenWithoutExp = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(tokenWithoutExp)).isTrue();
    }

    @Test
    void shouldReturnFalseForCompletelyInvalidJwt() {
        String invalidToken = "not.a.jwt.token";

        assertThat(jwtUtils.validateJwtToken(invalidToken)).isFalse();
    }


}
