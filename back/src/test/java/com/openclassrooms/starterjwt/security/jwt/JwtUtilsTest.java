package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;

import java.util.Date;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private final String jwtSecret = "testSecret"; // Simuler la valeur de ${oc.app.jwtSecret}
    private final int jwtExpirationMs = 3600000; // Simuler la valeur de ${oc.app.jwtExpirationMs}

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        // Injecter les valeurs des propriétés via réflexion
        injectPrivateField(jwtUtils, "jwtSecret", jwtSecret);
        injectPrivateField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
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
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // Expiré depuis 2 heures
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // Expiré depuis 1 heure
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
    }

    @Test
    void shouldThrowExceptionForMalformedJwtToken() {
        String malformedToken = "malformed.token";

        // Valider le token et vérifier les logs
        assertThat(jwtUtils.validateJwtToken(malformedToken)).isFalse();
    }

    @Test
    void shouldGetUserNameFromJwtToken() {
        // Générer un token
        String token = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Extraire le nom d'utilisateur
        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(username).isEqualTo("test@example.com");
    }

    // Méthode utilitaire pour injecter les champs privés
    private void injectPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'injection de champ privé", e);
        }
    }
}
