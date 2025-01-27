package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
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

    @Test
    void shouldReturnAllTeachers() {
        // Appeler le service pour récupérer tous les enseignants
        List<Teacher> teachers = teacherService.findAll();

        // Vérifier qu'il y a 2 enseignants
        assertThat(teachers).hasSize(2);
    }

    @Test
    void shouldFindTeacherById() {
        // Appeler le service pour récupérer un enseignant par ID
        Teacher teacher = teacherService.findById(1L);

        // Vérifier les résultats
        assertThat(teacher).isNotNull();
        assertThat(teacher.getFirstName()).isEqualTo("Margot");
    }
}
