package com.bank.authorization.integration.controller;


import com.bank.authorization.dto.UserDto;
import com.bank.authorization.integration.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {
    private static final Long ID = 1L;
    private static final Long ID_2 = 2L;

    private static final UserDto FIRST_DTO = new UserDto(ID,"test1", "test1",123L);
    private static final UserDto SECOND_DTO = new UserDto(3L,"test3", "test3", 12345L);

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SECOND_DTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.profileId", Matchers.is(12345)));
    }

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        mockMvc.perform(get("/read/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$.profileId", Matchers.is(123)));
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() throws Exception {
        mockMvc.perform(get("/read/13"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception {
        mockMvc.perform(put("/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FIRST_DTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.is(ID.intValue())))
                .andExpect(jsonPath("$.profileId", Matchers.is(123)));

    }

    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNotExistIdNegativeTest() throws Exception {
        mockMvc.perform(put("/1/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<Long> ids = List.of(ID, ID_2);

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
        mockMvc.perform(get("/read/all")
                        .param("ids", "2,3"))
                .andExpect(status().isNotFound());
    }
}