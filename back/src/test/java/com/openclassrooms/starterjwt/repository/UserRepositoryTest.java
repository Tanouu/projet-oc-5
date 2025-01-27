package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldMockSaveAndFindUser() {
        // Préparer les données
        User mockUser = new User()
                .setId(1L)
                .setFirstName("Ethan")
                .setLastName("Pacheco")
                .setEmail("ethan.pacheco@example.com")
                .setPassword("password123");

        // Configurer le comportement du mock
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(mockUser);

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));

        // Sauvegarder un utilisateur
        User savedUser = userRepository.save(new User()
                .setFirstName("Ethan")
                .setLastName("Pacheco")
                .setEmail("ethan.pacheco@example.com")
                .setPassword("password123"));

        // Vérifications
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);

        // Rechercher l'utilisateur par ID
        Optional<User> retrievedUser = userRepository.findById(1L);

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("ethan.pacheco@example.com");

        // Vérifier que les méthodes du mock ont été appelées
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void shouldMockFindByEmail() {
        // Préparer les données
        User mockUser = new User()
                .setId(2L)
                .setFirstName("Tanou")
                .setLastName("Pacheco")
                .setEmail("tanou.pacheco@example.com")
                .setPassword("password123");

        // Configurer le comportement du mock
        Mockito.when(userRepository.findByEmail("tanou.pacheco@example.com"))
                .thenReturn(Optional.of(mockUser));

        // Rechercher un utilisateur par email
        Optional<User> retrievedUser = userRepository.findByEmail("tanou.pacheco@example.com");

        // Vérifications
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getFirstName()).isEqualTo("Tanou");

        // Vérifier que la méthode du mock a été appelée
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("tanou.pacheco@example.com");
    }

    @Test
    void shouldMockCheckIfEmailExists() {
        // Configurer le comportement du mock
        Mockito.when(userRepository.existsByEmail("ethan.pacheco@example.com"))
                .thenReturn(true);
        Mockito.when(userRepository.existsByEmail("notfound@example.com"))
                .thenReturn(false);

        // Vérifier si les emails existent
        boolean exists = userRepository.existsByEmail("ethan.pacheco@example.com");
        boolean notExists = userRepository.existsByEmail("notfound@example.com");

        // Assertions
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();

        // Vérifier que la méthode du mock a été appelée
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail("ethan.pacheco@example.com");
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail("notfound@example.com");
    }
}
