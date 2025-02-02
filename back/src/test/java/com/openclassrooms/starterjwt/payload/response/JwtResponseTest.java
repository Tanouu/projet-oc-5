package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JwtResponseTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        // Arrange
        JwtResponse jwtResponse = new JwtResponse("accessToken123", 1L, "testUser", "Test", "User", true);

        // Act
        jwtResponse.setToken("newToken456");
        jwtResponse.setType("JWT");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("updatedUser");
        jwtResponse.setFirstName("Updated");
        jwtResponse.setLastName("Name");
        jwtResponse.setAdmin(false);

        // Assert
        assertThat(jwtResponse.getToken()).isEqualTo("newToken456");
        assertThat(jwtResponse.getType()).isEqualTo("JWT");
        assertThat(jwtResponse.getId()).isEqualTo(2L);
        assertThat(jwtResponse.getUsername()).isEqualTo("updatedUser");
        assertThat(jwtResponse.getFirstName()).isEqualTo("Updated");
        assertThat(jwtResponse.getLastName()).isEqualTo("Name");
        assertThat(jwtResponse.getAdmin()).isFalse();
    }
}
