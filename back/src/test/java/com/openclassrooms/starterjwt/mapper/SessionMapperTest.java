package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Teacher teacher;
    private User user1, user2;
    private SessionDto sessionDto;
    private Session session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'un enseignant fictif
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Margot");
        teacher.setLastName("DELAHAYE");

        // Création d'utilisateurs fictifs
        user1 = new User();
        user1.setId(101L);
        user1.setFirstName("Ethan");
        user1.setLastName("Pacheco");

        user2 = new User();
        user2.setId(102L);
        user2.setFirstName("Lucas");
        user2.setLastName("Kat");

        // Création d'une session DTO fictive
        sessionDto = new SessionDto();
        sessionDto.setName("Yoga Class");
        sessionDto.setDescription("A relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(101L, 102L));

        // Création d'une session fictive
        session = new Session();
        session.setName("Yoga Class");
        session.setDescription("A relaxing yoga session.");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        // Stubbing uniquement les méthodes utilisées
        lenient().when(teacherService.findById(1L)).thenReturn(teacher);
        lenient().when(userService.findById(101L)).thenReturn(user1);
        lenient().when(userService.findById(102L)).thenReturn(user2);
    }

    @Test
    void shouldMapSessionDtoToEntity() {
        Session entity = sessionMapper.toEntity(sessionDto);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Yoga Class");
        assertThat(entity.getDescription()).isEqualTo("A relaxing yoga session.");
        assertThat(entity.getTeacher()).isEqualTo(teacher);
        assertThat(entity.getUsers()).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void shouldMapSessionToDto() {
        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Yoga Class");
        assertThat(dto.getDescription()).isEqualTo("A relaxing yoga session.");
        assertThat(dto.getTeacher_id()).isEqualTo(1L);
        assertThat(dto.getUsers()).containsExactlyInAnyOrder(101L, 102L);
    }

    @Test
    void shouldReturnEmptyListWhenUsersAreNull() {
        session.setUsers(null);
        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto.getUsers()).isEmpty();
    }

    @Test
    void shouldHandleNullTeacher() {
        sessionDto.setTeacher_id(null);
        Session entity = sessionMapper.toEntity(sessionDto);

        assertThat(entity.getTeacher()).isNull();
    }

    /*** AJOUT DES TESTS MANQUANTS POUR 100% DE COUVERTURE ***/

    @Test
    void shouldMapSessionDtoListToEntityList() {
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto);
        List<Session> entities = sessionMapper.toEntity(sessionDtos);

        assertThat(entities).hasSize(1);
        assertThat(entities.get(0).getName()).isEqualTo("Yoga Class");
    }

    @Test
    void shouldMapSessionListToDtoList() {
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> dtos = sessionMapper.toDto(sessions);

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getName()).isEqualTo("Yoga Class");
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyListToEntity() {
        List<Session> entities = sessionMapper.toEntity(Collections.emptyList());
        assertThat(entities).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyListToDto() {
        List<SessionDto> dtos = sessionMapper.toDto(Collections.emptyList());
        assertThat(dtos).isEmpty();
    }

    @Test
    void shouldReturnNullTeacherIdIfSessionHasNoTeacher() {
        session.setTeacher(null);
        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto.getTeacher_id()).isNull();
    }

}
