package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUpDatabase() {
        // Initialisation des données avant TOUS les tests
        userRepository.save(new User()
                .setEmail("ethan@example.com")
                .setFirstName("Ethan")
                .setLastName("Pacheco")
                .setPassword("password123")
                .setAdmin(false)
        );

        userRepository.save(new User()
                .setEmail("tanou@example.com")
                .setFirstName("Tanou")
                .setLastName("Pacheco")
                .setPassword("password123")
                .setAdmin(true)
        );
    }

    @Test
    void shouldFindUserById() {
        // Appeler le service
        User foundUser = userService.findById(2L);

        // Vérifier les résultats
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("ethan@example.com");
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        // Appeler le service avec un ID inexistant
        User foundUser = userService.findById(999L);

        // Vérifier le résultat
        assertThat(foundUser).isNull();
    }

    @Test
    void shouldDeleteUserById() {
        // Supprimer un utilisateur
        userService.delete(1L);

        // Vérifier qu'il est supprimé
        assertThat(userRepository.findById(1L)).isEmpty();
    }

    @Test
    void shouldReturnAllUsers() {
        // Appeler le service pour récupérer tous les utilisateurs
        List<User> users = userRepository.findAll();

        // Vérifier qu'il y a 3 utilisateurs initiaux
        assertThat(users).hasSize(3);
    }

    @Test
    void shouldFailToSaveUserWithInvalidEmail() {
        // Essayer de sauvegarder un utilisateur avec un email invalide
        User invalidUser = new User()
                .setEmail("invalid-email")
                .setFirstName("Invalid")
                .setLastName("User")
                .setPassword("password123")
                .setAdmin(false);

        assertThatThrownBy(() -> userRepository.save(invalidUser))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    void shouldUpdateUserDetails() {
        // Mettre à jour un utilisateur existant
        User existingUser = userService.findById(2L);
        existingUser.setFirstName("UpdatedFirstName");

        userRepository.save(existingUser);

        // Vérifier les changements
        User updatedUser = userService.findById(2L);
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstName");
    }
}
