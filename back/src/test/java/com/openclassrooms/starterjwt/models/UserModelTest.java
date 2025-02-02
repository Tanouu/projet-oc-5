package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        user2 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        user3 = new User(2L, "other@example.com", "Smith", "Jane", "password456", false, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testConstructor() {
        User user = new User(10L, "user@example.com", "White", "Walter", "securepass", false, LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(user);
        assertEquals(10L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("White", user.getLastName());
        assertEquals("Walter", user.getFirstName());
        assertEquals("securepass", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(3L);
        user.setEmail("new@example.com");
        user.setLastName("Brown");
        user.setFirstName("Charlie");
        user.setPassword("newpass");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        assertEquals(3L, user.getId());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("Brown", user.getLastName());
        assertEquals("Charlie", user.getFirstName());
        assertEquals("newpass", user.getPassword());
        assertTrue(user.isAdmin());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testEquals() {
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testHashCode() {
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "User(id=1, email=test@example.com, lastName=Doe, firstName=John, password=password123, admin=true, createdAt=" + user1.getCreatedAt() + ", updatedAt=" + user1.getUpdatedAt() + ")";
        assertEquals(expectedString, user1.toString());
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .id(4L)
                .email("builder@example.com")
                .lastName("Black")
                .firstName("Jack")
                .password("builderpass")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(user);
        assertEquals(4L, user.getId());
        assertEquals("builder@example.com", user.getEmail());
        assertEquals("Black", user.getLastName());
        assertEquals("Jack", user.getFirstName());
        assertEquals("builderpass", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testCanEqual() {
        assertTrue(user1.canEqual(user2));
        assertFalse(user1.canEqual(new Object()));
    }

    @Test
    void testEqualsWithNullAndDifferentType() {
        assertNotEquals(user1, null);
        assertNotEquals(user1, "String");
    }

    @Test
    void testSettersWithNullValues() {
        assertThrows(NullPointerException.class, () -> user1.setEmail(null));
        assertThrows(NullPointerException.class, () -> user1.setPassword(null));
    }


    @Test
    void testUserBuilderWithPartialValues() {
        User user = User.builder()
                .id(6L)
                .email("partial@example.com")
                .lastName("Grey")
                .firstName("Alice")
                .password("defaultPassword") // Ajout d'un mot de passe pour éviter l'erreur
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(user);
        assertEquals("partial@example.com", user.getEmail());
        assertEquals("Grey", user.getLastName());
        assertEquals("Alice", user.getFirstName());
        assertNotNull(user.getPassword()); // Assurer que le mot de passe n'est pas null
    }


    @Test
    void testUserBuilderWithNullEmail() {
        assertThrows(NullPointerException.class, () ->
                User.builder()
                        .id(7L)
                        .lastName("NullEmail")
                        .firstName("Bob")
                        .password("password")
                        .admin(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }


    @Test
    void testUserBuilderWithNullPassword() {
        assertThrows(NullPointerException.class, () -> {
            User user = User.builder()
                    .id(10L)
                    .email("noPass@example.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password(null)
                    .build();
        });
    }


    @Test
    void testUserBuilderWithMissingValues() {
        User user = User.builder()
                .id(5L)
                .email("partial@example.com")
                .lastName("Doe")
                .firstName("Jake")
                .password("partialPass")
                .admin(false)
                .build();

        assertEquals("Doe", user.getLastName());
        assertEquals("partial@example.com", user.getEmail());
        assertEquals("Jake", user.getFirstName());
    }

    @Test
    void testUserConstructorWithMinimumFields() {
        User user = new User("minimal@example.com", "Doe", "John", "password123", false);
        assertNotNull(user);
        assertEquals("minimal@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testEqualsWithDifferentAttributes() {
        User user4 = new User(99L, "different@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user5 = new User(100L, "test@example.com", "Different", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(user1, user4); // ID différent
        assertNotEquals(user1, user5); // ID différent
    }


    @Test
    void testHashCodeWithDifferentObjects() {
        User user6 = new User(3L, "unique@example.com", "Unique", "User", "pass456", false, LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(user1.hashCode(), user6.hashCode());
    }

    @Test
    void testToStringFormat() {
        String result = user1.toString();
        assertNotNull(result);
        assertTrue(result.contains("User("));
        assertTrue(result.contains("test@example.com"));
    }

    @Test
    void testHashCodeWithDifferentUsers() {
        User user6 = new User(3L, "unique@example.com", "Unique", "Person", "pass", true, LocalDateTime.now(), LocalDateTime.now());
        assertNotEquals(user1.hashCode(), user6.hashCode());
    }

    @Test
    void testUserConstructorWithMinimalFields() {
        User user = new User("minimal@example.com", "Doe", "John", "password123", false);
        assertNotNull(user);
        assertEquals("minimal@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testUserConstructorWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 5, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 6, 1, 12, 30);

        User user = new User(20L, "full@example.com", "Taylor", "Chris", "securepass", true, createdAt, updatedAt);

        assertNotNull(user);
        assertEquals(20L, user.getId());
        assertEquals("full@example.com", user.getEmail());
        assertEquals("Taylor", user.getLastName());
        assertEquals("Chris", user.getFirstName());
        assertEquals("securepass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }


    @Test
    void testEqualsWithIdenticalValues() {
        User userCopy = new User(user1.getId(), user1.getEmail(), user1.getLastName(), user1.getFirstName(), user1.getPassword(), user1.isAdmin(), user1.getCreatedAt(), user1.getUpdatedAt());
        assertEquals(user1, userCopy);
    }


    @Test
    void testAdminSetter() {
        User user = new User();
        user.setAdmin(true);
        assertTrue(user.isAdmin());
        user.setAdmin(false);
        assertFalse(user.isAdmin());
    }

    @Test
    void testCreatedAtAndUpdatedAtSetters() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 5, 14, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 18, 30);

        User user = new User();
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    void testUserToStringWithEmptyValues() {
        User user = new User();
        user.setId(10L);
        user.setEmail("");
        user.setLastName("");
        user.setFirstName("");
        user.setPassword("");
        user.setAdmin(false);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);

        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("email="));
        assertTrue(result.contains("lastName="));
        assertTrue(result.contains("firstName="));
        assertTrue(result.contains("password="));
    }


    @Test
    void testUserBuilderToString() {
        User.UserBuilder builder = User.builder()
                .id(5L)
                .email("builder@example.com")
                .lastName("Black")
                .firstName("Jack")
                .password("securePass")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("User.UserBuilder"));
        assertTrue(builderString.contains("email=builder@example.com"));
        assertTrue(builderString.contains("lastName=Black"));
        assertTrue(builderString.contains("firstName=Jack"));
    }

    @Test
    void testUserBuilderSetters() {
        // Création d'un builder
        User.UserBuilder builder = User.builder()
                .id(6L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        // Appliquer les setters sur le même builder
        builder.id(10L);
        builder.email("setter@example.com");
        builder.lastName("SetterLastName");
        builder.firstName("SetterFirstName");
        builder.password("SetterPassword");
        builder.admin(true);

        LocalDateTime now = LocalDateTime.now();
        builder.createdAt(now);
        builder.updatedAt(now);

        // Créer un seul objet final et vérifier toutes les valeurs
        User user = builder.build();

        assertEquals(10L, user.getId());
        assertEquals("setter@example.com", user.getEmail());
        assertEquals("SetterLastName", user.getLastName());
        assertEquals("SetterFirstName", user.getFirstName());
        assertEquals("SetterPassword", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }


    @Test
    void testUserBuilderBuild() {
        User.UserBuilder builder = User.builder()
                .id(7L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("pass123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        User user = builder.build();

        assertNotNull(user);
        assertEquals(7L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("pass123", user.getPassword());
        assertTrue(user.isAdmin());
    }


}
