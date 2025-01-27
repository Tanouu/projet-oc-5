package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authTokenFilter = new AuthTokenFilter();

        // Injecter les mocks dans les champs privés
        injectPrivateField(authTokenFilter, "jwtUtils", jwtUtils);
        injectPrivateField(authTokenFilter, "userDetailsService", userDetailsService);

        // Réinitialiser le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Permet d'accéder aux champs privés
        field.set(target, value);
    }

    @Test
    void shouldAuthenticateUserWhenJwtIsValid() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer mockJwtToken");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("mockJwtToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("mockJwtToken")).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, times(1)).validateJwtToken("mockJwtToken");
        verify(jwtUtils, times(1)).getUserNameFromJwtToken("mockJwtToken");
        verify(userDetailsService, times(1)).loadUserByUsername("testUser");

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);

        // Verify filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUserWhenJwtIsInvalid() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidJwtToken");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("invalidJwtToken")).thenReturn(false);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, times(1)).validateJwtToken("invalidJwtToken");
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(anyString());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Verify filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUserWhenNoJwtProvided() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest(); // Pas de header Authorization
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(); // Vérifie que rien n'est défini

        // Vérifie que les dépendances mockées ne sont pas appelées
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(anyString());

        // Vérifie que la chaîne de filtres continue
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
