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

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();  // Ensuite supprimer les utilisateurs
        setUpDatabase();
    }

    void setUpDatabase() {
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
        User firstUser = userRepository.findAll().get(0);
        User foundUser = userService.findById(firstUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("ethan@example.com");
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        User foundUser = userService.findById(999L);
        assertThat(foundUser).isNull();
    }

    @Test
    void shouldDeleteUserById() {
        User firstUser = userRepository.findAll().get(0);
        userService.delete(firstUser.getId());

        assertThat(userRepository.findById(firstUser.getId())).isEmpty();
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldFailToSaveUserWithInvalidEmail() {
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
        User existingUser = userRepository.findAll().get(0);
        existingUser.setFirstName("UpdatedFirstName");

        userRepository.save(existingUser);

        User updatedUser = userService.findById(existingUser.getId());
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstName");
    }
}
