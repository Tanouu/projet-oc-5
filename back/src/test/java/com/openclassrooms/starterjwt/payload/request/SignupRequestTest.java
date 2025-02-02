package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    @Test
    void shouldVerifyEqualsAndHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("newuser@example.com");
        request1.setFirstName("New");
        request1.setLastName("User");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("newuser@example.com");
        request2.setFirstName("New");
        request2.setLastName("User");
        request2.setPassword("password123");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("anotheruser@example.com");
        request3.setFirstName("Another");
        request3.setLastName("Person");
        request3.setPassword("password456");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }

    @Test
    void shouldVerifyToString() {
        SignupRequest request = new SignupRequest();
        request.setEmail("newuser@example.com");
        request.setFirstName("New");
        request.setLastName("User");
        request.setPassword("password123");

        String requestString = request.toString();
        assertThat(requestString).contains("newuser@example.com", "New", "User", "password123");
    }

    @Test
    void shouldTestEqualsForVariousCases() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPassword("password123");

        assertThat(request).isNotEqualTo(null);

        assertThat(request).isNotEqualTo(new Object());

        assertThat(request).isEqualTo(request);

        SignupRequest differentRequest = new SignupRequest();
        differentRequest.setEmail("other@example.com"); // Email différent
        differentRequest.setFirstName("Test");
        differentRequest.setLastName("User");
        differentRequest.setPassword("password123");

        assertThat(request).isNotEqualTo(differentRequest);
    }

    @Test
    void shouldTestDifferentHashCodes() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("user1@example.com");
        request1.setFirstName("User1");
        request1.setLastName("Last1");
        request1.setPassword("pass123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("user2@example.com"); // Différent
        request2.setFirstName("User1");
        request2.setLastName("Last1");
        request2.setPassword("pass123");

        assertThat(request1.hashCode()).isNotEqualTo(request2.hashCode());
    }

    @Test
    void shouldTestCanEqual() {
        SignupRequest request1 = new SignupRequest();
        SignupRequest request2 = new SignupRequest();
        Object otherObject = new Object();

        assertThat(request1.canEqual(request2)).isTrue();
        assertThat(request1.canEqual(otherObject)).isFalse();
    }

    @Test
    void shouldTestEqualsWithNullValues() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail(null);
        request1.setFirstName(null);
        request1.setLastName(null);
        request1.setPassword(null);

        SignupRequest request2 = new SignupRequest();
        request2.setEmail(null);
        request2.setFirstName(null);
        request2.setLastName(null);
        request2.setPassword(null);

        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void shouldTestHashCodeWithNullValues() {
        SignupRequest request = new SignupRequest();
        request.setEmail(null);
        request.setFirstName(null);
        request.setLastName(null);
        request.setPassword(null);

        assertThat(request.hashCode()).isNotNull();
    }

    @Test
    void shouldTestEqualsWithCompletelyDifferentObject() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPassword("password123");

        assertThat(request).isNotEqualTo("Just a string"); // Tester un objet totalement différent
    }

    @Test
    void shouldTestEqualsWithPartialData() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("email@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("email@example.com");

        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void shouldTestEqualsWithNullAndFilledObject() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail(null);
        request1.setFirstName(null);
        request1.setLastName(null);
        request1.setPassword(null);

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("email@example.com");
        request2.setFirstName("First");
        request2.setLastName("Last");
        request2.setPassword("password");

        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void shouldTestEqualsWithOneDifferentField() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("Test");
        request1.setLastName("User");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("Test");
        request2.setLastName("User");
        request2.setPassword("differentPassword"); // Un seul champ différent

        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void shouldTestEqualsWithClonedObject() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("Test");
        request1.setLastName("User");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail(new String("test@example.com")); // Nouvelle instance mais même valeur
        request2.setFirstName(new String("Test"));
        request2.setLastName(new String("User"));
        request2.setPassword(new String("password123"));

        assertThat(request1).isEqualTo(request2);
    }



}
