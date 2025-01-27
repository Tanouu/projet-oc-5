package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();

        // Récupérer le premier enseignant existant
        Teacher teacher = teacherRepository.findAll().get(0);

        // Créer une session liée à cet enseignant
        Session session = new Session();
        session.setName("Yoga Class");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);

        // Sauvegarder la session dans le repository
        sessionRepository.save(session);
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
        sessionDto.setTeacher_id(teacherRepository.findAll().get(0).getId());
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
        sessionDto.setTeacher_id(teacherRepository.findAll().get(0).getId());
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
    void shouldDeleteSession() throws Exception {
        Session session = sessionRepository.findAll().get(0);

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }
}
