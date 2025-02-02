package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void shouldMapUserToDto() {
        User user = User.builder()
                .id(1L)
                .email("ethan@example.com")
                .lastName("Pacheco")
                .firstName("Ethan")
                .password("password123")
                .admin(true)
                .build();

        UserDto dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("Ethan");
        assertThat(dto.getLastName()).isEqualTo("Pacheco");
        assertThat(dto.getEmail()).isEqualTo("ethan@example.com");
        assertThat(dto.isAdmin()).isTrue();
    }

    @Test
    void shouldMapDtoToUser() {
        UserDto dto = new UserDto(1L, "ethan@example.com", "Pacheco", "Ethan", true, "password123", null, null);

        User user = userMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("Ethan");
        assertThat(user.getLastName()).isEqualTo("Pacheco");
        assertThat(user.getEmail()).isEqualTo("ethan@example.com");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldMapUserListToDtoList() {
        User user1 = User.builder()
                .id(1L)
                .email("ethan@example.com")
                .lastName("Pacheco")
                .firstName("Ethan")
                .password("password123")
                .admin(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("lucas@example.com")
                .lastName("Kat")
                .firstName("Lucas")
                .password("password456")
                .admin(false)
                .build();

        List<User> users = Arrays.asList(user1, user2);

        List<UserDto> dtos = userMapper.toDto(users);

        assertThat(dtos).isNotNull();
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getFirstName()).isEqualTo("Ethan");
        assertThat(dtos.get(1).getFirstName()).isEqualTo("Lucas");
    }

    @Test
    void shouldMapDtoListToUserList() {
        UserDto dto1 = new UserDto(1L, "ethan@example.com", "Pacheco", "Ethan", true, "password123", null, null);
        UserDto dto2 = new UserDto(2L, "lucas@example.com", "Kat", "Lucas", false, "password123", null, null);
        List<UserDto> dtos = Arrays.asList(dto1, dto2);

        List<User> users = userMapper.toEntity(dtos);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getFirstName()).isEqualTo("Ethan");
        assertThat(users.get(1).getFirstName()).isEqualTo("Lucas");
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        assertThat(userMapper.toEntity((UserDto) null)).isNull(); // Cast explicite pour éviter l'ambiguïté
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyList() {
        List<UserDto> emptyDtoList = Collections.emptyList();
        List<User> emptyUserList = Collections.emptyList();

        assertThat(userMapper.toEntity(emptyDtoList)).isEmpty();
        assertThat(userMapper.toDto(emptyUserList)).isEmpty();
    }
}
