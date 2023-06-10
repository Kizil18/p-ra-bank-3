package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.service.AccountTransferService;
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
import org.webjars.NotFoundException;

import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(controllers = AccountTransferController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AccountTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountTransferService service;

    @Autowired
    private ObjectMapper mapper;

    private AccountTransferDto dto;
    private AccountTransferDto secondDto;

    @BeforeEach
    void setUp() {
        dto = new AccountTransferDto(3L, 1L, BigDecimal.valueOf(1),
                "give", 1L);
        secondDto = new AccountTransferDto(4L, 1L, BigDecimal.valueOf(1),
                "give", 1L);
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<AccountTransferDto> dtoList = List.of(dto, secondDto);
        List<Long> ids = List.of(3L, 4L);
        when(service.findAllById(ids)).thenReturn(dtoList);

        ResultActions response = mockMvc.perform(get("/account/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "3, 4"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(3)))
                .andExpect(jsonPath("$[1].id", Matchers.is(4)));
    }

    @Test
    @DisplayName("чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNotExistIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        when(service.findAllById(ids))
                .thenThrow(new NullPointerException("Пользователей c такими ID не существует"));
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        when(service.findById(dto.getId())).thenReturn(dto);

        mockMvc.perform(get("/account/read/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.accountNumber", Matchers.is(1)))
                .andExpect(jsonPath("$.amount", Matchers.is(1)))
                .andExpect(jsonPath("$.purpose", Matchers.is("give")))
                .andExpect(jsonPath("$.accountDetailsId", Matchers.is(1)));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() {
        when(service.findById(2L)).thenThrow(new NotFoundException("Пользователя с таким ID не существует"));
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        given(service.save(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.accountNumber", Matchers.is(1)));
    }

    @Test
    @DisplayName("создание существующего пользователя, негативный тест")
    void createExistingUserNegativeTest() {
        when(service.save(dto)).thenThrow(new NullPointerException("Такой пользователь уже существует"));
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception {
        when(service.update(dto.getId(), dto)).thenReturn(dto);

        ResultActions response = mockMvc.perform(put("/account/update/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("обновление по чужому id, негативный тест")
    void updateByAnotherIdNegativeTest() {
        when(service.update(4L, dto))
                .thenThrow(new NullPointerException("Данный ID принадлежит другому пользователю"));
    }
}