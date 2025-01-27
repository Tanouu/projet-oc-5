package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {


    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
        userRepository.save(new User()
                .setFirstName("Admin")
                .setLastName("Admin")
                .setEmail("yoga@studio.com")
                .setPassword("password_hash")
                .setAdmin(true));
    }

    @BeforeEach
    void setUpData() {
        userRepository.save(new User()
                .setFirstName("User1")
                .setLastName("Last1")
                .setEmail("user1@example.com")
                .setPassword("password123")
                .setAdmin(false));

        userRepository.save(new User()
                .setFirstName("User2")
                .setLastName("Last2")
                .setEmail("user2@example.com")
                .setPassword("password456")
                .setAdmin(false));
    }


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldReturnUserWhenFoundById() throws Exception {
        // Récupérer un utilisateur existant dans la base
        User user = userRepository.findByEmail("yoga@studio.com")
                .orElseThrow(() -> new NotFoundException());


        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("yoga@studio.com"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenUserNotFoundById() throws Exception {
        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldReturnBadRequestForInvalidIdOnFindById() throws Exception {
        mockMvc.perform(get("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldDeleteUserWhenAuthenticatedAndAuthorized() throws Exception {
        // Récupérer un utilisateur existant dans la base
        User user = userRepository.findByEmail("yoga@studio.com")
                .orElseThrow(() -> new NotFoundException());

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"USER"})
    void shouldReturnUnauthorizedWhenUserNotAuthorizedToDelete() throws Exception {
        // Récupérer un utilisateur existant dans la base
        User user = userRepository.findByEmail("yoga@studio.com")
                .orElseThrow(() -> new NotFoundException());

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());

        Optional<User> existingUser = userRepository.findById(user.getId());
        assertThat(existingUser).isPresent();
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {
        mockMvc.perform(delete("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldReturnBadRequestForInvalidIdOnDelete() throws Exception {
        mockMvc.perform(delete("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }
}
