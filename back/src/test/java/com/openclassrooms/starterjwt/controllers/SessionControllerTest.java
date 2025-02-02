package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        teacher = teacherRepository.save(new Teacher()
                .setFirstName("Margot")
                .setLastName("DELAHAYE")
        );

        sessionRepository.save(new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A relaxing yoga session.")
                .setTeacher(teacher)
                .setUsers(new ArrayList<>())
        );

        userRepository.save(new User()
                .setEmail("ethan@example.com")
                .setFirstName("Ethan")
                .setLastName("Pacheco")
                .setPassword("password123")
                .setAdmin(false)
        );
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldFindSessionById() throws Exception {
        Session session = sessionRepository.findAll().get(0);

        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Class"))
                .andExpect(jsonPath("$.description").value("A relaxing yoga session."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnNotFoundForInvalidSessionId() throws Exception {
        mockMvc.perform(get("/api/session/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldCreateSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Pilates Class");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setDescription("A core-strengthening pilates session.");

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pilates Class"))
                .andExpect(jsonPath("$.description").value("A core-strengthening pilates session."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldUpdateSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Yoga Class");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setDescription("An updated relaxing yoga session.");

        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Yoga Class"))
                .andExpect(jsonPath("$.description").value("An updated relaxing yoga session."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnBadRequestWhenUpdatingWithInvalidId() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Yoga Class");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacherRepository.findAll().get(0).getId());
        sessionDto.setDescription("An updated relaxing yoga session.");

        mockMvc.perform(put("/api/session/invalid_id") // ID invalide
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest()); // Vérifie que la réponse est 400 Bad Request
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnOkWhenUpdatingNonExistentSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Yoga Class");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacherRepository.findAll().get(0).getId());
        sessionDto.setDescription("An updated relaxing yoga session.");

        mockMvc.perform(put("/api/session/999") // ID valide mais session inexistante
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk()); // Vérifie que la réponse est bien 200 OK
    }


    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldDeleteSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldAllowUserToParticipateInSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(session.getId()).orElseThrow();
        assertThat(updatedSession.getUsers()).contains(user);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnAllSessions() throws Exception {
        // Vérifier que findAll fonctionne et retourne les sessions créées dans setUp()
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // On s'attend à 1 session (celle du setUp)
                .andExpect(jsonPath("$[0].name").value("Yoga Class"))
                .andExpect(jsonPath("$[0].description").value("A relaxing yoga session."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnEmptyListWhenNoSessionsExist() throws Exception {
        // Supprimer toutes les sessions pour tester le cas où findAll retourne une liste vide
        sessionRepository.deleteAll();

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // Liste vide
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnBadRequestForNonNumericSessionId() throws Exception {
        mockMvc.perform(get("/api/session/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenSavingNonExistentSession() throws Exception {
        mockMvc.perform(post("/api/session/999/save"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenParticipatingInNonExistentSession() throws Exception {
        User user = userRepository.findAll().get(0);

        mockMvc.perform(post("/api/session/999/participate/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldCallSaveMethod() throws Exception {
        mockMvc.perform(post("/api/session/1/save"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindSessionInRepository() {
        Session session = sessionRepository.findAll().get(0);
        Optional<Session> foundSession = sessionRepository.findById(session.getId());
        assertTrue(foundSession.isPresent(), "La session devrait exister !");
    }


    @Test
    @WithMockUser(username = "ethan@example.com", roles = {"USER"})
    void shouldAllowNewUserToParticipateInSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(session.getId()).orElseThrow();
        assertThat(updatedSession.getUsers()).contains(user);
    }

    @Test
    @WithMockUser(username = "ethan@example.com", roles = {"USER"})
    void shouldReturnBadRequestWhenUserAlreadyParticipates() throws Exception {
        Session session = sessionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        session.getUsers().add(user);
        sessionRepository.save(session);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "ethan@example.com", roles = {"USER"})
    void shouldAllowUserToNoLongerParticipateInSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        session.getUsers().add(user);
        sessionRepository.save(session);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(session.getId()).orElseThrow();
        assertThat(updatedSession.getUsers()).doesNotContain(user);
    }

    @Test
    @WithMockUser(username = "ethan@example.com", roles = {"USER"})
    void shouldReturnNotFoundWhenLeavingNonExistentSession() throws Exception {
        User user = userRepository.findAll().get(0);

        mockMvc.perform(delete("/api/session/999/participate/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "ethan@example.com", roles = {"USER"})
    void shouldReturnBadRequestWhenUserIsNotInSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnBadRequestWhenDeletingWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/session/invalid_id"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistentSession() throws Exception {
        mockMvc.perform(delete("/api/session/9999")) // ID inexistant
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldDeleteSessionSuccessfully() throws Exception {
        Session session = sessionRepository.findAll().get(0);

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        // Vérifier que la session n'existe plus en base
        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }



}
