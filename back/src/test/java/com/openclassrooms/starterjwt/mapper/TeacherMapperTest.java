package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    void shouldMapTeacherToDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Margot");
        teacher.setLastName("DELAHAYE");

        // When
        TeacherDto dto = teacherMapper.toDto(teacher);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("Margot");
        assertThat(dto.getLastName()).isEqualTo("DELAHAYE");
    }

    @Test
    void shouldMapDtoToTeacher() {
        // Given
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("Margot");
        dto.setLastName("DELAHAYE");

        // When
        Teacher teacher = teacherMapper.toEntity(dto);

        // Then
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("Margot");
        assertThat(teacher.getLastName()).isEqualTo("DELAHAYE");
    }

    @Test
    void shouldMapTeacherListToDtoList() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Margot");
        teacher1.setLastName("DELAHAYE");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Lucas");
        teacher2.setLastName("Kat");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // When
        List<TeacherDto> dtos = teacherMapper.toDto(teachers);

        // Then
        assertThat(dtos).isNotNull().hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldMapDtoListToTeacherList() {
        // Given
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("Margot");
        dto1.setLastName("DELAHAYE");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Lucas");
        dto2.setLastName("Kat");

        List<TeacherDto> dtos = Arrays.asList(dto1, dto2);

        // When
        List<Teacher> teachers = teacherMapper.toEntity(dtos);

        // Then
        assertThat(teachers).isNotNull().hasSize(2);
        assertThat(teachers.get(0).getId()).isEqualTo(1L);
        assertThat(teachers.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        assertThat(teacherMapper.toEntity((TeacherDto) null)).isNull();
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyList() {
        assertThat(teacherMapper.toDto(List.of())).isEmpty();
        assertThat(teacherMapper.toEntity(List.of())).isEmpty();
    }
}
