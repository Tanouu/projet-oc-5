package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SessionRepositoryTest {

    @MockBean
    private SessionRepository sessionRepository;

    @Test
    void shouldMockSaveAndFindSession() {
        // Préparer les données
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Ethan")
                .setLastName("Pacheco");

        Session mockSession = new Session()
                .setId(1L)
                .setName("Yoga Basics")
                .setDate(new Date())
                .setDescription("Introduction to yoga.")
                .setTeacher(teacher);

        // Configurer le comportement du mock
        Mockito.when(sessionRepository.save(Mockito.any(Session.class)))
                .thenReturn(mockSession);

        Mockito.when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(mockSession));

        // Sauvegarder une session
        Session savedSession = sessionRepository.save(new Session()
                .setName("Yoga Basics")
                .setDate(new Date())
                .setDescription("Introduction to yoga.")
                .setTeacher(teacher));

        // Vérifications
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isEqualTo(1L);

        // Rechercher la session par ID
        Optional<Session> retrievedSession = sessionRepository.findById(1L);

        assertThat(retrievedSession).isPresent();
        assertThat(retrievedSession.get().getName()).isEqualTo("Yoga Basics");

        // Vérifier que les méthodes du mock ont été appelées
        Mockito.verify(sessionRepository, Mockito.times(1)).save(Mockito.any(Session.class));
        Mockito.verify(sessionRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void shouldMockFindAllSessions() {
        // Préparer les données
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Tanou")
                .setLastName("Pacheco");

        Session session1 = new Session()
                .setId(1L)
                .setName("Yoga Basics")
                .setDate(new Date())
                .setDescription("Introduction to yoga.")
                .setTeacher(teacher);

        Session session2 = new Session()
                .setId(2L)
                .setName("Advanced Yoga")
                .setDate(new Date())
                .setDescription("Advanced poses and techniques.")
                .setTeacher(teacher);

        List<Session> mockSessions = Arrays.asList(session1, session2);

        // Configurer le comportement du mock
        Mockito.when(sessionRepository.findAll()).thenReturn(mockSessions);

        // Rechercher toutes les sessions
        List<Session> retrievedSessions = sessionRepository.findAll();

        // Vérifications
        assertThat(retrievedSessions).isNotNull();
        assertThat(retrievedSessions.size()).isEqualTo(2);
        assertThat(retrievedSessions.get(0).getName()).isEqualTo("Yoga Basics");
        assertThat(retrievedSessions.get(1).getName()).isEqualTo("Advanced Yoga");

        // Vérifier que la méthode du mock a été appelée
        Mockito.verify(sessionRepository, Mockito.times(1)).findAll();
    }
}
