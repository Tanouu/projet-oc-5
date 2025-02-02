package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TeacherModelTest {

    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        teacher3 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testConstructor() {
        Teacher teacher = new Teacher(10L, "White", "Walter", LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(teacher);
        assertEquals(10L, teacher.getId());
        assertEquals("White", teacher.getLastName());
        assertEquals("Walter", teacher.getFirstName());
    }

    @Test
    void testGettersAndSetters() {
        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setLastName("Brown");
        teacher.setFirstName("Charlie");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        assertEquals(3L, teacher.getId());
        assertEquals("Brown", teacher.getLastName());
        assertEquals("Charlie", teacher.getFirstName());
        assertNotNull(teacher.getCreatedAt());
        assertNotNull(teacher.getUpdatedAt());
    }

    @Test
    void testEquals() {
        assertEquals(teacher1, teacher2);
        assertNotEquals(teacher1, teacher3);
    }

    @Test
    void testHashCode() {
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode());
    }

    @Test
    void testBuilder() {
        Teacher teacher = Teacher.builder()
                .id(4L)
                .lastName("White")
                .firstName("Walter")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(teacher);
        assertEquals(4L, teacher.getId());
        assertEquals("White", teacher.getLastName());
        assertEquals("Walter", teacher.getFirstName());
    }

    @Test
    void testCanEqual() {
        assertTrue(teacher1.canEqual(teacher2));
        assertFalse(teacher1.canEqual(new Object()));
    }

    @Test
    void testToStringWithAllFields() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 5, 1, 10, 0);

        Teacher teacher = new Teacher(5L, "Williams", "Serena", fixedTime, fixedTime);

        String expectedString = "Teacher(id=5, lastName=Williams, firstName=Serena, createdAt="
                + fixedTime + ", updatedAt=" + fixedTime + ")";

        assertEquals(expectedString, teacher.toString());
    }

    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        Teacher teacher = new Teacher();

        // Assert - Vérifie que l'objet est bien instancié
        assertNotNull(teacher, "L'objet Teacher ne doit pas être null après instanciation.");

        // Vérifie que les valeurs par défaut sont bien nulles
        assertNull(teacher.getId(), "L'id doit être null par défaut.");
        assertNull(teacher.getLastName(), "Le nom doit être null par défaut.");
        assertNull(teacher.getFirstName(), "Le prénom doit être null par défaut.");
        assertNull(teacher.getCreatedAt(), "createdAt doit être null par défaut.");
        assertNull(teacher.getUpdatedAt(), "updatedAt doit être null par défaut.");
    }

    @Test
    void testToString() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "Doe", "John", now, now);

        // Act
        String result = teacher.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Teacher"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("lastName=Doe"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("createdAt=" + now));
        assertTrue(result.contains("updatedAt=" + now));

        System.out.println("Test toString() : " + result);
    }

    @Test
    void testToStringWithNullFields() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName(null);
        teacher.setFirstName(null);
        teacher.setCreatedAt(null);
        teacher.setUpdatedAt(null);

        // Act
        String result = teacher.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Teacher"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("lastName=null"));
        assertTrue(result.contains("firstName=null"));
        assertTrue(result.contains("createdAt=null"));
        assertTrue(result.contains("updatedAt=null"));

        System.out.println("Test toString() with null fields: " + result);
    }

    @Test
    void testTeacherBuilderToString() {
        Teacher.TeacherBuilder builder = Teacher.builder()
                .id(5L)
                .lastName("Black")
                .firstName("Jack")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("Teacher.TeacherBuilder"));
    }

    @Test
    void testEqualsWithDifferentAttributes() {
        Teacher teacher4 = new Teacher(2L, "Doe", "Jane", LocalDateTime.now(), LocalDateTime.now());
        assertNotEquals(teacher1, teacher4); // Ici, teacher1 a un id=1 et teacher4 un id=2, donc ils seront bien différents
    }


    @Test
    void testEqualsWithNullValues() {
        Teacher teacherWithNullLastName = new Teacher(2L, null, "John", LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(teacher1, teacherWithNullLastName); // IDs différents, donc equals() renverra false
    }




}
