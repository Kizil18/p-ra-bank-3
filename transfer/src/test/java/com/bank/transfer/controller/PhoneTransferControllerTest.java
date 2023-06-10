package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.service.PhoneTransferService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PhoneTransferController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PhoneTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneTransferService service;

    @Autowired
    private ObjectMapper mapper;

    private PhoneTransferDto dto;
    private PhoneTransferDto secondDto;

    @BeforeEach
    void setUp() {
        dto = new PhoneTransferDto(1L, 8L, BigDecimal.valueOf(1),
                "purpose", 1L);
        secondDto = new PhoneTransferDto(2L, 8L, BigDecimal.valueOf(1),
                "purpose", 1L);
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception{
        List<PhoneTransferDto> dtoList = List.of(dto, secondDto);
        List<Long> ids = List.of(1L, 2L);

        when(service.findAllById(ids)).thenReturn(dtoList);

        ResultActions response = mockMvc.perform(get("/phone/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1, 2"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNotExistIdNegativeTest() {
        List<Long> ids = List.of(2L, 3L);
        when(service.findAllById(ids)).thenThrow(new NotFoundException("Пользователей c такими ID не существует"));
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        when(service.findById(dto.getId())).thenReturn(dto);

        mockMvc.perform(get("/phone/read/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is(8)))
                .andExpect(jsonPath("$.amount", Matchers.is(1)))
                .andExpect(jsonPath("$.purpose", Matchers.is("purpose")))
                .andExpect(jsonPath("$.accountDetailsId", Matchers.is(1)));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() {
        when(service.findById(3L)).thenThrow(new NotFoundException("Пользователя с таким ID не существует"));
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception{
        given(service.save(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/phone/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is(8)));
    }

    @Test
    @DisplayName("создание существующей сущности, негативный тест")
    void createExistingEntityNegativeTest() {
        when(service.save(dto)).thenThrow(new NullPointerException("Такой пользователь уже существует"));
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception{
        when(service.update(dto.getId(), dto)).thenReturn(dto);

        ResultActions response = mockMvc.perform(put("/phone/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("обновление по чужому id, негативный тест")
    void updateByAnotherIdIdNegativeTest() {
        when(service.update(2L, dto))
                .thenThrow(new NullPointerException("Данный ID принадлежит другому пользователю"));
    }
}