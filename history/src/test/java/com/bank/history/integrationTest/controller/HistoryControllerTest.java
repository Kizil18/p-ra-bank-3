package com.bank.history.integrationTest.controller;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.repository.HistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@AutoConfigureMockMvc
@SpringBootTest
public class HistoryControllerTest {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        HistoryEntity historyEntity1 = new HistoryEntity(1L, 2L, 2L, 2L,
                2L, 2L, 2L);
        HistoryEntity historyEntity2 = new HistoryEntity(2L, 5L, 5L, 5L,
                5L, 5L, 5L);
        historyRepository.save(historyEntity1);
        historyRepository.save(historyEntity2);
    }

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    public void getByIdPositiveTest() throws Exception {
        int idHistory = 1;
        ResultActions response = mockMvc.perform(get("/api/history/" + idHistory )
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("Чтение по id равному Null, негативный сценарий")
    public void getByIdNegativeTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/history/" + null)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Чтение по нескольким id, позитивный сценарий")
    public void getAllByIdTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1, 2"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("Чтение по нескольким id равным null, негативный сценарий")
    void readAllByNullIdNegativeTest() throws Exception{
        ResultActions response = mockMvc.perform(get("/api/history" + null)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    public void updateTest() throws Exception {
        int idHistory = 1;
        HistoryDto expectedDto = new HistoryDto(3L, 3L, 3L, 3L,
                3L, 3L, 3L);

        ResultActions response = mockMvc.perform(put("/api/history/" + idHistory)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Обновление null, негативный сценарий")
    void updateNullNegativeTest() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/history/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        HistoryDto expectedDto = new HistoryDto(3L, 3L, 3L, 3L,
                3L, 3L, 3L);

        ResultActions response = mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.transferAuditId", Matchers.is(3)));
    }

    @Test
    @DisplayName("Создание, негативный сценарий")
    void createNegativeTest() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
