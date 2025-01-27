package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();

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
                .setAdmin(false)
        );
    }

    @Test
    void shouldInitializeDatabaseWithTeachersAndUsers() {
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(2);
        assertThat(teachers.get(0).getFirstName()).isEqualTo("Margot");
        assertThat(teachers.get(1).getFirstName()).isEqualTo("Hélène");

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldCreateSession() {
        Teacher teacher = teacherRepository.findAll().get(0);
        Session session = new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A beginner yoga session")
                .setTeacher(teacher);

        Session createdSession = sessionService.create(session);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Yoga Class");
        assertThat(createdSession.getTeacher().getFirstName()).isEqualTo("Margot");
    }

    @Test
    void shouldNotCreateSessionWithoutMandatoryFields() {
        Session invalidSession = new Session()
                .setDate(new Date())
                .setDescription("Description without name or teacher");

        assertThatThrownBy(() -> sessionService.create(invalidSession))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFindAllSessions() {
        Teacher teacher = teacherRepository.findAll().get(0);
        sessionService.create(new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A beginner yoga session")
                .setTeacher(teacher)
        );

        List<Session> sessions = sessionService.findAll();
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Yoga Class");
    }

    @Test
    void shouldDeleteSession() {
        Teacher teacher = teacherRepository.findAll().get(0);
        Session session = sessionService.create(new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A beginner yoga session")
                .setTeacher(teacher)
        );

        sessionService.delete(session.getId());
        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }

    @Test
    void shouldParticipateInSession() {
        Teacher teacher = teacherRepository.findAll().get(0);
        Session session = sessionService.create(new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A beginner yoga session")
                .setTeacher(teacher)
        );

        User user = userRepository.findAll().get(0);
        sessionService.participate(session.getId(), user.getId());

        Session updatedSession = sessionService.getById(session.getId());
        assertThat(updatedSession.getUsers()).isNotEmpty();
        assertThat(updatedSession.getUsers().get(0).getEmail()).isEqualTo("ethan@example.com");
    }

    @Test
    void shouldRemoveParticipationFromSession() {
        Teacher teacher = teacherRepository.findAll().get(0);
        Session session = sessionService.create(new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A beginner yoga session")
                .setTeacher(teacher)
        );

        User user = userRepository.findAll().get(0);
        sessionService.participate(session.getId(), user.getId());
        sessionService.noLongerParticipate(session.getId(), user.getId());

        Session updatedSession = sessionService.getById(session.getId());
        assertThat(updatedSession.getUsers()).isEmpty();
    }
}
