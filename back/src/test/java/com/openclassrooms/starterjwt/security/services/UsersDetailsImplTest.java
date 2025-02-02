package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsersDetailsImplTest {

    @Test
    void equals_shouldReturnTrue_whenObjectsAreEqual() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password")
                .build();

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldReturnFalse_whenObjectsAreDifferent() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("other@example.com")
                .password("password")
                .build();

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void getAdmin_shouldReturnTrue_whenUserIsAdmin() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("admin@example.com")
                .password("password")
                .admin(true)
                .build();

        assertThat(user.getAdmin()).isTrue();
    }

    @Test
    void getAdmin_shouldReturnFalse_whenUserIsNotAdmin() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .password("password")
                .admin(false)
                .build();

        assertThat(user.getAdmin()).isFalse();
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password")
                .build();

        assertThat(user.toString()).isNotEmpty();
    }

    @Test
    void builder_shouldBuildUserWithAllFields() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(true)
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getAdmin()).isTrue();
    }

    @Test
    void builder_shouldBuildUserWithNullValues() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getFirstName()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getAdmin()).isNull();
    }

    @Test
    void getAuthorities_shouldReturnEmptyCollection() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.getAuthorities()).isNotNull();
        assertThat(user.getAuthorities()).isEmpty();
    }

    @Test
    void userDetails_shouldAlwaysBeEnabledAndNotLocked() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void toString_shouldNotBeNull() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password")
                .build();

        assertThat(user.toString()).isNotNull();
    }

    @Test
    void testUserDetailsImplBuilderToString() {
        UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
                .id(5L)
                .username("builder@example.com")
                .password("builderpass")
                .admin(false);

        String builderString = builder.toString();

        assertNotNull(builderString);
        assertTrue(builderString.contains("UserDetailsImpl.UserDetailsImplBuilder"));
    }


}
