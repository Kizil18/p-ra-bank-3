package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = BankDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BankDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankDetailsService service;
    @Autowired
    private ObjectMapper objectMapper;
    private BankDetailsDto bankDetailsDto;
    private BankDetailsDto bankDetailsDto2;

    @BeforeEach
    void init() {
        BigDecimal bigDecimal = new BigDecimal("30100000000000000001");

        bankDetailsDto = BankDetailsDto.builder()
                .id(1L).bik(44444444L)
                .inn(1111111111L).kpp(111111111L)
                .corAccount(bigDecimal)
                .city("Kazan").jointStockCompany("company")
                .name("name").build();
        bankDetailsDto2 = BankDetailsDto.builder()
                .id(2L).bik(44444444L)
                .inn(1111111111L).kpp(111111111L)
                .corAccount(bigDecimal)
                .city("Kazan").jointStockCompany("company")
                .name("name").build();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        Mockito.when(service.findById(id)).thenReturn(bankDetailsDto);

        ResultActions response = mockMvc.perform(get("/bank/details/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankDetailsDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.city", Matchers.is("Kazan")));
    }

    @Test
    @DisplayName("чтение по id, негативный сценарий")
    void readByNonExpectIdNegativeTest() throws Exception {
        when(service.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/bank/details/")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<BankDetailsDto> dtoList = List.of(bankDetailsDto, bankDetailsDto2);
        Mockito.when(service.findAllById(ids)).thenReturn(dtoList);

        ResultActions response = mockMvc.perform(get("/bank/details/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ids", "1, 2"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("чтение по нескольким id, негативный сценарий")
    void readAllByNonExpectIdNegativeTest() throws Exception {
        when(service.findAllById(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/bank/details/read/all")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        given(service.create(ArgumentMatchers.any())).willAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));
        ResultActions response = mockMvc.perform(post("/bank/details/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankDetailsDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.city", Matchers.is("Kazan")));
    }

    @Test
    @DisplayName("создание, негативный сценарий")
    void createNegativeTest() throws Exception {
        when(service.create(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/bank/details/create")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        Long id = 1L;
        Mockito.when(service.update(id, bankDetailsDto)).thenReturn(bankDetailsDto);
        ResultActions response = mockMvc.perform(put("/bank/details/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankDetailsDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.city", Matchers.is("Kazan")));
    }

    @Test
    @DisplayName("обновление, негативный сценарий")
    void updateNegativeTest() throws Exception {
        when(service.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(put("/bank/details/update/1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}