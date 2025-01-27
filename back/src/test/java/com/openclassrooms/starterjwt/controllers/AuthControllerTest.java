package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAdmin(false);
        userRepository.save(user);
    }


    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void shouldReturnErrorForInvalidCredentials() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpassword");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Vérification que l'utilisateur a bien été enregistré
        Optional<User> newUser = userRepository.findByEmail("newuser@example.com");
        assertThat(newUser).isPresent();
        assertThat(newUser.get().getFirstName()).isEqualTo("New");
        assertThat(newUser.get().getLastName()).isEqualTo("User");
        assertThat(passwordEncoder.matches("password123", newUser.get().getPassword())).isTrue();
    }

    @Test
    void shouldReturnErrorForExistingEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com"); // Email déjà existant dans le setup
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Existing");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        // Vérification que le nombre d'utilisateurs en base reste à 1
        assertThat(userRepository.count()).isEqualTo(1);
    }


    @Test
    void shouldReturnErrorForInvalidSignupRequest() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(""); // Email vide
        signupRequest.setPassword("short"); // Mot de passe trop court
        signupRequest.setFirstName("");
        signupRequest.setLastName("");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }


}

