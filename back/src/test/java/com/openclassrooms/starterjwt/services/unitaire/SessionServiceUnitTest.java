package com.openclassrooms.starterjwt.services.unitaire;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceUnitTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session testSession;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("ethan@example.com");

        testSession = new Session();
        testSession.setId(1L);
        testSession.setName("Yoga Class");
        testSession.setUsers(Arrays.asList(testUser)); // Ajout d'un utilisateur
    }

    @Test
    void shouldCreateSession() {
        when(sessionRepository.save(testSession)).thenReturn(testSession);

        Session createdSession = sessionService.create(testSession);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Yoga Class");

        verify(sessionRepository, times(1)).save(testSession);
    }

    @Test
    void shouldDeleteSessionById() {
        doNothing().when(sessionRepository).deleteById(1L);

        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldFindAllSessions() {
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(testSession));

        List<Session> sessions = sessionService.findAll();

        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Yoga Class");

        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnSessionById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        Session foundSession = sessionService.getById(1L);

        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getId()).isEqualTo(1L);

        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(1L);

        assertThat(foundSession).isNull();

        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateSession() {
        when(sessionRepository.save(testSession)).thenReturn(testSession);

        Session updatedSession = sessionService.update(1L, testSession);

        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getId()).isEqualTo(1L);

        verify(sessionRepository, times(1)).save(testSession);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenParticipatingInNonExistentSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.participate(1L, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.participate(1L, 2L))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenUserAlreadyParticipates() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> sessionService.participate(1L, 1L))
                .isInstanceOf(BadRequestException.class);

        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenLeavingNonExistentSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenUserIsNotParticipating() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        testSession.setUsers(Arrays.asList(anotherUser));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 1L))
                .isInstanceOf(BadRequestException.class);

        verify(sessionRepository, times(1)).findById(1L);
    }
}
