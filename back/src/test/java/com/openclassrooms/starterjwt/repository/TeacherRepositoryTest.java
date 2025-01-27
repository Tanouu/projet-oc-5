package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeacherRepositoryTest {

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void shouldMockSaveAndFindTeacher() {
        // Préparer les données
        Teacher mockTeacher = new Teacher()
                .setId(1L)
                .setFirstName("Ethan")
                .setLastName("Pacheco");

        // Configurer le comportement du mock
        Mockito.when(teacherRepository.save(Mockito.any(Teacher.class)))
                .thenReturn(mockTeacher);

        Mockito.when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(mockTeacher));

        // Sauvegarder un enseignant
        Teacher savedTeacher = teacherRepository.save(new Teacher()
                .setFirstName("Ethan")
                .setLastName("Pacheco"));

        // Vérifications
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isEqualTo(1L);

        // Rechercher l'enseignant par ID
        Optional<Teacher> retrievedTeacher = teacherRepository.findById(1L);

        assertThat(retrievedTeacher).isPresent();
        assertThat(retrievedTeacher.get().getLastName()).isEqualTo("Pacheco");

        // Vérifier que les méthodes du mock ont été appelées
        Mockito.verify(teacherRepository, Mockito.times(1)).save(Mockito.any(Teacher.class));
        Mockito.verify(teacherRepository, Mockito.times(1)).findById(1L);
    }
}
