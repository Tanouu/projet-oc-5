package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenAllFieldsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("SecurePass123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidation_whenEmailIsBlank() {
        LoginRequest request = new LoginRequest();
        request.setEmail(""); // Champ vide
        request.setPassword("SecurePass123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .anyMatch(message -> message.contains("ne doit pas être vide") || message.contains("must not be blank"));
    }

    @Test
    void shouldFailValidation_whenPasswordIsBlank() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword(""); // Champ vide

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .anyMatch(message -> message.contains("ne doit pas être vide") || message.contains("must not be blank"));
    }
}
