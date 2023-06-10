package com.bank.authorization.controller;


import com.bank.authorization.dto.UserDto;
import com.bank.authorization.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final Long ID = 1L;
    private static final Long ID_2 = 2L;

    private static final UserDto FIRST_DTO = new UserDto(ID,"", "123",1L);
    private static final UserDto SECOND_DTO = new UserDto(ID_2,"", "321", 2L);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        when(userService.save(FIRST_DTO)).thenReturn(FIRST_DTO);

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FIRST_DTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$.profileId", Matchers.is(1)));
    }

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        when(userService.findById(ID)).thenReturn(FIRST_DTO);

        mockMvc.perform(get("/read/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$.profileId", Matchers.is(1)));
    }

    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() throws Exception {
        when(userService.findById(ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/read/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        when(userService.update(ID, FIRST_DTO)).thenReturn(FIRST_DTO);

        mockMvc.perform(put("/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FIRST_DTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$.profileId", Matchers.is(1)));

    }

    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNotExistIdNegativeTest() throws Exception {
        when(userService.update(ID, FIRST_DTO)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/1/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<Long> ids = List.of(ID, ID_2);
        when(userService.findAllByIds(ids)).thenReturn(List.of(FIRST_DTO, SECOND_DTO));

        mockMvc.perform(get("/read/all")
                .param("ids", "1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$[1].id", Matchers.is(ID_2.intValue())));
    }

    @Test
    @DisplayName("Поиск по нескольким несуществующим id, негативный сценарий")
    void readAllByNotExistIdNegativeTest() throws Exception {
        when(userService.findAllByIds(anyList())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/read/all")
                        .param("ids", "2,3"))
                .andExpect(status().isNotFound());
    }
}