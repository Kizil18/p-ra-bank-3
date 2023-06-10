package com.bank.authorization.service;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Long ID = 1L;
    private static final Long ID_2 = 2L;
    private static final UserEntity FIRST_ENTITY = new UserEntity(ID, "", 1L, "123");
    private static final UserEntity SECOND_ENTITY = new UserEntity(ID_2, "", 2L, "321");
    private static final UserDto FIRST_DTO = new UserDto(ID, "", "123", 1L);
    private static final UserDto SECOND_DTO = new UserDto(ID_2, "", "321", 2L);

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Тест поиска UserDto по id, позитивный сценарий")
    void findByIdPositiveTest() {
        when(repository.findById(ID)).thenReturn(Optional.of(FIRST_ENTITY));
        when(mapper.toDTO(FIRST_ENTITY)).thenReturn(FIRST_DTO);

        UserDto userDto = userService.findById(ID);

        assertNotNull(userDto);
        assertEquals(userDto, FIRST_DTO);
        verify(repository).findById(ID);
    }

    @Test
    @DisplayName(" поиск по id, негативный сценарий")
    void findByIdNegativeTest_ThrowsException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(ID);
        });
    }

    @Test
    @DisplayName("Тест сохраниения нового UserDto, позитивный сценарий")
    void savePositiveTest() {
        when(mapper.toEntity(FIRST_DTO)).thenReturn(FIRST_ENTITY);
        when(repository.save(FIRST_ENTITY)).thenReturn(FIRST_ENTITY);
        when(mapper.toDTO(FIRST_ENTITY)).thenReturn(FIRST_DTO);

        UserDto userDto = userService.save(FIRST_DTO);

        verify(mapper).toEntity(FIRST_DTO);
        verify(repository).save(FIRST_ENTITY);
        verify(mapper).toDTO(FIRST_ENTITY);

        assertNotNull(userDto);
        assertEquals(userDto, FIRST_DTO);
    }

    @Test
    @DisplayName("Тест обновления по id, позитивный сценарий")
    void updatePositiveTest() {
        when(repository.findById(ID)).thenReturn(Optional.of(FIRST_ENTITY));
        when(repository.save(FIRST_ENTITY)).thenReturn(FIRST_ENTITY);
        when(mapper.mergeToEntity(FIRST_DTO, FIRST_ENTITY)).thenReturn(FIRST_ENTITY);
        when(mapper.toDTO(FIRST_ENTITY)).thenReturn(FIRST_DTO);

        UserDto userDto = userService.update(ID, FIRST_DTO);

        verify(repository).findById(ID);
        verify(repository).save(FIRST_ENTITY);
        verify(mapper).mergeToEntity(FIRST_DTO, FIRST_ENTITY);
        verify(mapper).toDTO(FIRST_ENTITY);

        assertNotNull(userDto);
        assertEquals(userDto, FIRST_DTO);
    }

    @Test
    @DisplayName(" обновление по id, негативный сценарий")
    void updateNegativeTest_ThrowsException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.update(ID, FIRST_DTO);
        });
    }

    @Test
    @DisplayName("Тест поиска нескольких UserDto по нескольким id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        List<Long> ids = List.of(ID, ID_2);
        List<UserEntity> users = List.of(FIRST_ENTITY, SECOND_ENTITY);
        List<UserDto> userDtos = List.of(FIRST_DTO, SECOND_DTO);
        when(repository.findById(ID)).thenReturn(Optional.of(FIRST_ENTITY));
        when(repository.findById(ID_2)).thenReturn(Optional.of(SECOND_ENTITY));
        when(mapper.toDtoList(users)).thenReturn(List.of(FIRST_DTO, SECOND_DTO));

        List<UserDto> userDtoList = userService.findAllByIds(ids);

        verify(repository).findById(ID);
        verify(repository).findById(ID_2);
        verify(mapper).toDtoList(users);

        assertNotNull(userDtoList);
        assertEquals(userDtos, userDtoList);
    }

    @Test
    @DisplayName("поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNotExistIdsNegativeTest() {
        List<Long> ids = List.of(ID, ID_2);
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findAllByIds(ids);
        });
    }
}