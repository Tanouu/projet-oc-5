package com.openclassrooms.starterjwt.services.unitaire;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceUnitTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
    }

    @Test
    void shouldReturnAllTeachers_WhenTeachersExist() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertThat(teachers).isNotNull();
        assertThat(teachers).hasSize(2);
        assertThat(teachers).containsExactly(teacher1, teacher2);

        // Vérification que findAll a bien été appelé
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_WhenNoTeachersExist() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(List.of());

        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertThat(teachers).isEmpty();

        // Vérification que findAll a bien été appelé
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTeacher_WhenTeacherExists() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        // Act
        Teacher foundTeacher = teacherService.findById(1L);

        // Assert
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getId()).isEqualTo(1L);
        assertThat(foundTeacher.getFirstName()).isEqualTo("John");
        assertThat(foundTeacher.getLastName()).isEqualTo("Doe");

        // Vérification que findById a bien été appelé une fois
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNull_WhenTeacherDoesNotExist() {
        // Arrange
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Teacher foundTeacher = teacherService.findById(999L);

        // Assert
        assertThat(foundTeacher).isNull();

        // Vérification que findById a bien été appelé une fois
        verify(teacherRepository, times(1)).findById(999L);
    }
}
