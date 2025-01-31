package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void cleanDatabase() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        setUpDatabase();
    }

    void setUpDatabase() {
        teacherRepository.save(new Teacher().setFirstName("Margot").setLastName("DELAHAYE"));
        teacherRepository.save(new Teacher().setFirstName("Hélène").setLastName("THIERCELIN"));
    }

    @Test
    void shouldReturnAllTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        assertThat(teachers).hasSize(2);
    }

    @Test
    void shouldFindTeacherById() {
        Teacher firstTeacher = teacherRepository.findAll().get(0);
        Teacher foundTeacher = teacherService.findById(firstTeacher.getId());

        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getFirstName()).isEqualTo("Margot");
    }

    @Test
    void shouldReturnNullWhenTeacherNotFound() {
        Teacher foundTeacher = teacherService.findById(999L); // ID inexistant
        assertThat(foundTeacher).isNull();
    }

    @Test
    void shouldDeleteAllTeachers() {
        teacherRepository.deleteAll();
        List<Teacher> teachers = teacherService.findAll();

        assertThat(teachers).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoTeachers() {
        teacherRepository.deleteAll();
        List<Teacher> teachers = teacherService.findAll();

        assertThat(teachers).isEmpty();
    }
}
