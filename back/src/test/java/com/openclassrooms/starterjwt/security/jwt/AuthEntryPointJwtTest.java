package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AuthEntryPointJwtTest {

    private final AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUnauthorizedResponse() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        authEntryPointJwt.commence(request, response, new org.springframework.security.core.AuthenticationException("Test unauthorized") {});

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo("application/json");
        String responseBody = response.getContentAsString();

        assertThat(responseBody).contains("\"status\":401");
        assertThat(responseBody).contains("\"error\":\"Unauthorized\"");
        assertThat(responseBody).contains("\"message\":\"Test unauthorized\"");
        assertThat(responseBody).contains("\"path\":\"/api/protected\"");
    }
}
