package com.bank.account.controller;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.service.AccountDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountDetailsController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AccountDetailsControllerTest {
    private static final Long ID = 1L;
    private AccountDetailsDto dto1;
    private AccountDetailsDto dto2;
    @Autowired
    private ObjectMapper objectMapper;
    private List<AccountDetailsDto> accountDtos;
    private List<Long> accountIds;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDetailsService service;

    @BeforeEach
    void prepare() {
        dto1 = new AccountDetailsDto(ID, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        dto2 = new AccountDetailsDto(2L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        accountIds = List.of(dto1.getId(), dto2.getId());
        accountDtos = List.of(dto1, dto2);
    }

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Mockito.when(service.findById(dto1.getId())).thenReturn(dto1);

        mockMvc.perform(MockMvcRequestBuilders.get("/details/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankDetailsId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money").value(BigDecimal.ONE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeBalance").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId").value(1L));
    }

    @Test
    @DisplayName("Чтение по id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        when(service.findById(ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/details/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        doReturn(dto1).when(service).save(dto1);
        mockMvc.perform(MockMvcRequestBuilders.post("/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportId").value(dto1.getPassportId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(dto1.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankDetailsId").value(dto1.getBankDetailsId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money").value(dto1.getMoney()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeBalance").
                        value(dto1.getNegativeBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId").value(dto1.getProfileId()));

    }

    @Test
    @DisplayName("Создание, негативный сценарий")
    void createNegativeTest() throws Exception {
        Mockito.when(service.save(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/details/create")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        doReturn(dto1).when(service).update(dto1.getId(), dto1);
        mockMvc.perform(MockMvcRequestBuilders.put("/details/update/" + dto1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportId").value(dto1.getPassportId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(dto1.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankDetailsId").value(dto1.getBankDetailsId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money").value(dto1.getMoney()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeBalance")
                        .value(dto1.getNegativeBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId").value(dto1.getProfileId()));
    }

    @Test
    @DisplayName("Обновление, негативный сценарий")
    void updateNegativeTest() throws Exception {
        Mockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/details/update/" + dto1.getId())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Чтение по нескольким id, позитивный сценарий")
    void readAllPositiveTest() throws Exception {
        doReturn(accountDtos).when(service).findAllById(accountIds);
        mockMvc.perform(MockMvcRequestBuilders.get("/details/read/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", dto1.getId().toString(), dto2.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(dto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].passportId").value(dto1.getPassportId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber").value(dto1.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bankDetailsId").value(dto1.getBankDetailsId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].money").value(dto1.getMoney()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].negativeBalance")
                        .value(dto1.getNegativeBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].profileId").value(dto1.getProfileId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(dto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].passportId").value(dto2.getPassportId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountNumber").value(dto2.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].bankDetailsId").value(dto2.getBankDetailsId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].money").value(dto2.getMoney()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].negativeBalance")
                        .value(dto2.getNegativeBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].profileId").value(dto2.getProfileId()));
    }
}