package com.openclassrooms.starterjwt.services.unitaire;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("ethan@example.com");
        testUser.setFirstName("Ethan");
        testUser.setLastName("Pacheco");
        testUser.setPassword("securePassword123");
        testUser.setAdmin(false);
    }

    @Test
    void shouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.findById(1L);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1L);
        assertThat(foundUser.getEmail()).isEqualTo("ethan@example.com");

        // Vérification que findById a bien été appelé une fois
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        User foundUser = userService.findById(1L);

        // Assert
        assertThat(foundUser).isNull();

        // Vérification que findById a bien été appelé une fois
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCallDelete_WhenUserExists() {
        // Arrange
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotThrowException_WhenDeletingNonExistingUser() {
        // Arrange
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}
