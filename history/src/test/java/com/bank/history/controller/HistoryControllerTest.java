package com.bank.history.controller;

import com.bank.history.service.HistoryService;
import com.bank.history.dto.HistoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = HistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @MockBean
    private HistoryService historyService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private HistoryDto historyDto;
    private HistoryDto historyDto1;

    @BeforeEach
    public void init() {
        historyDto = new HistoryDto(
                123L, 1L, 2L, 3L,
                4L, 5L, 6L);
        historyDto1 = new HistoryDto(
                456L, 1L, 2L, 3L,
                4L, 5L,6L);
    }

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        given(historyService.create(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historyDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(123)))
                .andExpect(jsonPath("$.transferAuditId", Matchers.is(1)));
    }

    @Test
    @DisplayName("Создание, негативный сценарий")
    void createNegativeTest() throws Exception {
        when(historyService.create(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        when(historyService.readById(historyDto.getId())).thenReturn(historyDto);

        mockMvc.perform(get("/api/history/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(123)))
                .andExpect(jsonPath("$.transferAuditId", Matchers.is(1)))
                .andExpect(jsonPath("$.profileAuditId", Matchers.is(2)))
                .andExpect(jsonPath("$.accountAuditId", Matchers.is(3)))
                .andExpect(jsonPath("$.antiFraudAuditId", Matchers.is(4)))
                .andExpect(jsonPath("$.publicBankInfoAuditId", Matchers.is(5)))
                .andExpect(jsonPath("$.authorizationAuditId", Matchers.is(6)));
    }

    @Test
    @DisplayName("Чтение по id равному null, негативный сценарий")
    void readByNullIdNegativeTest() throws Exception {
        when(historyService.readById(anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/api/history/")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        when(historyService.update(historyDto.getId(), historyDto)).thenReturn(historyDto);

        ResultActions response = mockMvc.perform(put("/api/history/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historyDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Обновление null, негативный сценарий")
    void updateNullNegativeTest() throws Exception {
        when(historyService.update(any(), any())).thenReturn(null);

        ResultActions response = mockMvc.perform(put("/api/history/123")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<HistoryDto> historyDtoList = List.of(historyDto, historyDto1);
        List<Long> listHistoryId = List.of(123L, 456L);
        when(historyService.readAllById(listHistoryId)).thenReturn(historyDtoList);

        ResultActions response = mockMvc.perform(get("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "123, 456"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(123)))
                .andExpect(jsonPath("$[1].id", Matchers.is(456)));
    }

    @Test
    @DisplayName("Чтение по нескольким id равным null, негативный сценарий")
    void readAllByNullIdNegativeTest() throws Exception{
        when(historyService.readAllById(any())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/api/history")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}