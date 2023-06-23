package com.bank.authorization.integration.service;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.integration.IntegrationTestBase;
import com.bank.authorization.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class UserServiceImplIT extends IntegrationTestBase {
    private static final Long ID = 1L;
    private static final Long ID_2 = 2L;
    private static final UserDto FIRST_DTO = new UserDto(ID, "test1", "test1", 123L);
    private static final UserDto SECOND_DTO = new UserDto(ID_2, "test2", "test2", 1234L);


    private final UserServiceImpl userService;

    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        UserDto userDto = userService.findById(ID);

        assertNotNull(userDto);
        assertEquals(userDto, FIRST_DTO);
    }

    @Test
    @DisplayName("Поиск по id, негативный сценарий")
    void findByIdNegativeTest_ThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(3L);
        });
    }

    @Test
    @DisplayName("Сохранение, позитивный сценарий")
    void savePositiveTest() {
        UserDto userDto = userService.save(new UserDto(3L, "test3", "test3", 321L));

        assertNotNull(userDto);
        assertEquals(userDto, userService.findById(3L));
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() {
        UserDto userDto = userService.update(ID, FIRST_DTO);

        assertNotNull(userDto);
        assertEquals(userDto, FIRST_DTO);
    }

    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.update(3L, FIRST_DTO);
        });
    }

    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        List<Long> ids = List.of(ID, ID_2);
        List<UserDto> usersDto = List.of(FIRST_DTO, SECOND_DTO);

        List<UserDto> userDtoList = userService.findAllByIds(ids);

        assertNotNull(userDtoList);
        assertEquals(usersDto, userDtoList);
    }

    @Test
    @DisplayName("поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNotExistIdsNegativeTest() {
        List<Long> ids = List.of(3L, 4L);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findAllByIds(ids);
        });
    }
}