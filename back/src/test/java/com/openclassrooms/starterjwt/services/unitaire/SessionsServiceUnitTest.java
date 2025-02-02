//package com.openclassrooms.starterjwt.services;
//
//import com.openclassrooms.starterjwt.exception.BadRequestException;
//import com.openclassrooms.starterjwt.exception.NotFoundException;
//import com.openclassrooms.starterjwt.models.Session;
//import com.openclassrooms.starterjwt.models.User;
//import com.openclassrooms.starterjwt.repository.SessionRepository;
//import com.openclassrooms.starterjwt.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SessionsServiceUnitTest {
//
//    @Mock
//    private SessionRepository mockSessionRepository;
//
//    @Mock
//    private UserRepository mockUserRepository;
//
//    @InjectMocks
//    private SessionService sessionService;
//
//    private Session mockSession;
//    private User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        mockUser = new User().setId(1L).setEmail("ethan@example.com");
//
//        mockSession = new Session()
//                .setId(1L)
//                .setName("Yoga Class")
//                .setDate(new Date())
//                .setDescription("A beginner yoga session")
//                .setUsers(Arrays.asList(mockUser));
//    }
//
//    @Test
//    void shouldThrowNotFoundException_WhenSessionNotFound() {
//        when(mockSessionRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> sessionService.getById(1L))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessageContaining("Session not found");
//    }
//
//    @Test
//    void shouldThrowBadRequestException_WhenUserAlreadyParticipates() {
//        when(mockSessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));
//        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(mockUser));
//
//        assertThatThrownBy(() -> sessionService.participate(1L, 1L))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("User already participates in session");
//    }
//
//    @Test
//    void shouldThrowNotFoundException_WhenRemovingParticipationButSessionNotFound() {
//        when(mockSessionRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 1L))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessageContaining("Session not found");
//    }
//
//    @Test
//    void shouldRemoveParticipation_WhenUserExists() {
//        when(mockSessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));
//
//        sessionService.noLongerParticipate(1L, 1L);
//
//        assertThat(mockSession.getUsers()).doesNotContain(mockUser);
//        verify(mockSessionRepository, times(1)).save(any(Session.class));
//    }
//
//}
