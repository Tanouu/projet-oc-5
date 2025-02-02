package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionModelTest {

    private Session session1;
    private Session session2;
    private Session session3;

    @BeforeEach
    void setUp() {
        session1 = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga", null, List.of(), LocalDateTime.now(), LocalDateTime.now());
        session2 = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga", null, List.of(), LocalDateTime.now(), LocalDateTime.now());
        session3 = new Session(2L, "Meditation Session", new Date(), "Guided meditation", null, List.of(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Session session = new Session();
        session.setId(3L);
        session.setName("Pilates Session");
        session.setDate(new Date());
        session.setDescription("Core strengthening Pilates");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        // Act & Assert
        assertEquals(3L, session.getId());
        assertEquals("Pilates Session", session.getName());
        assertEquals("Core strengthening Pilates", session.getDescription());
        assertNotNull(session.getCreatedAt());
        assertNotNull(session.getUpdatedAt());
    }

    @Test
    void testEquals() {
        assertEquals(session1, session2);
        assertNotEquals(session1, session3);
    }

    @Test
    void testHashCode() {
        assertEquals(session1.hashCode(), session2.hashCode());
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }

    @Test
    void testBuilder() {
        Session session = Session.builder()
                .id(4L)
                .name("Zumba Session")
                .date(new Date())
                .description("Fun and energetic Zumba class")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(session);
        assertEquals(4L, session.getId());
        assertEquals("Zumba Session", session.getName());
        assertEquals("Fun and energetic Zumba class", session.getDescription());
    }

    @Test
    void testToString() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 5, 1, 10, 0);
        Date fixedDate = new Date(1714563600000L);

        Session session = new Session(1L, "Yoga Session", fixedDate, "Relaxing yoga", null, List.of(), fixedTime, fixedTime);
        String result = session.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Yoga Session"));
        assertTrue(result.contains("description=Relaxing yoga"));
        assertTrue(result.contains("teacher=null"));
        assertTrue(result.contains("users=[]"));
    }

    @Test
    void testConstructorWithAllParams() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 5, 1, 10, 0);
        Date fixedDate = new Date(1714563600000L);

        Session session = new Session(1L, "Yoga Session", fixedDate, "Relaxing yoga", null, List.of(), fixedTime, fixedTime);

        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals(fixedDate, session.getDate());
        assertEquals("Relaxing yoga", session.getDescription());
        assertNull(session.getTeacher());
        assertEquals(List.of(), session.getUsers());
        assertEquals(fixedTime, session.getCreatedAt());
        assertEquals(fixedTime, session.getUpdatedAt());
    }


    @Test
    void testToStringExactOutput() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 5, 1, 10, 0);
        Date fixedDate = new Date(1714563600000L);

        Session session = new Session(1L, "Yoga Session", fixedDate, "Relaxing yoga", null, List.of(), fixedTime, fixedTime);

        String expectedString = "Session(id=1, name=Yoga Session, date=" + fixedDate +
                ", description=Relaxing yoga, teacher=null, users=[], createdAt=" + fixedTime +
                ", updatedAt=" + fixedTime + ")";

        assertEquals(expectedString, session.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime fixedTime = LocalDateTime.now();
        Date fixedDate = new Date();

        Session sessionA = new Session(1L, "Yoga", fixedDate, "Relax", null, List.of(), fixedTime, fixedTime);
        Session sessionB = new Session(1L, "Yoga", fixedDate, "Relax", null, List.of(), fixedTime, fixedTime);
        Session sessionC = new Session(2L, "Pilates", fixedDate, "Stretch", null, List.of(), fixedTime, fixedTime);

        assertEquals(sessionA, sessionB);
        assertNotEquals(sessionA, sessionC);

        assertEquals(sessionA.hashCode(), sessionB.hashCode());
        assertNotEquals(sessionA.hashCode(), sessionC.hashCode());
    }

    @Test
    void testSetters() {
        Session session = new Session();
        LocalDateTime fixedTime = LocalDateTime.now();
        Date fixedDate = new Date();

        session.setId(5L);
        session.setName("Advanced Yoga");
        session.setDate(fixedDate);
        session.setDescription("Deep relaxation");
        session.setCreatedAt(fixedTime);
        session.setUpdatedAt(fixedTime);

        assertEquals(5L, session.getId());
        assertEquals("Advanced Yoga", session.getName());
        assertEquals(fixedDate, session.getDate());
        assertEquals("Deep relaxation", session.getDescription());
        assertEquals(fixedTime, session.getCreatedAt());
        assertEquals(fixedTime, session.getUpdatedAt());
    }

    @Test
    void testSessionBuilder() {
        Session.SessionBuilder builder = Session.builder()
                .id(6L)
                .name("Beginner Yoga")
                .date(new Date())
                .description("Introduction to yoga")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        assertNotNull(builder);
    }

    @Test
    void testSessionBuilderSetters() {
        Session.SessionBuilder builder = Session.builder();

        builder.id(7L);
        builder.name("Intermediate Yoga");
        builder.date(new Date());
        builder.description("A more advanced yoga session");
        builder.createdAt(LocalDateTime.now());
        builder.updatedAt(LocalDateTime.now());

        Session session = builder.build();

        assertEquals(7L, session.getId());
        assertEquals("Intermediate Yoga", session.getName());
        assertEquals("A more advanced yoga session", session.getDescription());
        assertNotNull(session.getDate());
        assertNotNull(session.getCreatedAt());
        assertNotNull(session.getUpdatedAt());
    }

    @Test
    void testSessionBuilderToString() {
        Session.SessionBuilder builder = Session.builder()
                .id(8L)
                .name("Evening Yoga")
                .date(new Date())
                .description("Relaxing evening yoga")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        String builderString = builder.toString();

        assertNotNull(builderString);
        assertTrue(builderString.contains("Session.SessionBuilder"));
        assertTrue(builderString.contains("name=Evening Yoga"));
    }

    @Test
    void testSessionBuilderBuild() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();

        Session session = Session.builder()
                .id(9L)
                .name("Morning Yoga")
                .date(date)
                .description("Wake up with yoga")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(session);
        assertEquals(9L, session.getId());
        assertEquals("Morning Yoga", session.getName());
        assertEquals("Wake up with yoga", session.getDescription());
        assertEquals(date, session.getDate());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }


}
