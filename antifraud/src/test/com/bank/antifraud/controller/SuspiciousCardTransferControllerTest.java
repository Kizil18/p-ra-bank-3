package com.bank.antifraud.controller;

import com.bank.antifraud.common.data.SuspiciousCardTransferSupplier;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SuspiciousCardTransferController.class)
class SuspiciousCardTransferControllerTest {
    private final static String path = "/suspicious/card/transfer";
    private SuspiciousCardTransferDto transferDto;
    private SuspiciousCardTransferDto transferDto2;
    private List<SuspiciousCardTransferDto> transferDtos;
    private List<Long> transferIds;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SuspiciousCardTransferService service;

    @BeforeEach
    void prepare() {
        SuspiciousCardTransferSupplier supplier = new SuspiciousCardTransferSupplier();
        transferDto = supplier.getTransferDto(25L, 30L, false, true,
                "A", "B");
        transferDto2 = supplier.getTransferDto(35L, 40L, true, false,
                "C", "D");
        transferDtos = supplier.getTransferDtos(transferDto, transferDto2);
        transferIds = List.of(transferDto.getId(), transferDto2.getId());
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        doReturn(transferDto).when(service).findById(transferDto.getId());

        mockMvc.perform(get(path + "/" + transferDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(transferDto.getId().intValue())))
                .andExpect(jsonPath("$.cardTransferId",
                        Matchers.is(transferDto.getCardTransferId().intValue())))
                .andExpect(jsonPath("$.isBlocked", Matchers.is(transferDto.getIsBlocked())))
                .andExpect(jsonPath("$.isSuspicious", Matchers.is(transferDto.getIsSuspicious())))
                .andExpect(jsonPath("$.blockedReason", Matchers.is(transferDto.getBlockedReason())))
                .andExpect(jsonPath("$.suspiciousReason", Matchers.is(transferDto.getSuspiciousReason())));
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        doReturn(transferDtos).when(service).findAllById(transferIds);

        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", transferDto.getId().toString(), transferDto2.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", Matchers.is(transferDto.getId().intValue())))
                .andExpect(jsonPath("$[0].cardTransferId",
                        Matchers.is(transferDto.getCardTransferId().intValue())))
                .andExpect(jsonPath("$[0].isBlocked", Matchers.is(transferDto.getIsBlocked())))
                .andExpect(jsonPath("$[0].isSuspicious", Matchers.is(transferDto.getIsSuspicious())))
                .andExpect(jsonPath("$[0].blockedReason", Matchers.is(transferDto.getBlockedReason())))
                .andExpect(jsonPath("$[0].suspiciousReason", Matchers.is(transferDto.getSuspiciousReason())))
                .andExpect(jsonPath("$[1].id", Matchers.is(transferDto2.getId().intValue())))
                .andExpect(jsonPath("$[1].cardTransferId",
                        Matchers.is(transferDto2.getCardTransferId().intValue())))
                .andExpect(jsonPath("$[1].isBlocked", Matchers.is(transferDto2.getIsBlocked())))
                .andExpect(jsonPath("$[1].isSuspicious", Matchers.is(transferDto2.getIsSuspicious())))
                .andExpect(jsonPath("$[1].blockedReason", Matchers.is(transferDto2.getBlockedReason())))
                .andExpect(jsonPath("$[1].suspiciousReason", Matchers.is(transferDto2.getSuspiciousReason())));
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        doReturn(transferDto).when(service).save(transferDto);

        mockMvc.perform(post(path + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(transferDto.getId().intValue())))
                .andExpect(jsonPath("$.cardTransferId",
                        Matchers.is(transferDto.getCardTransferId().intValue())))
                .andExpect(jsonPath("$.isBlocked", Matchers.is(transferDto.getIsBlocked())))
                .andExpect(jsonPath("$.isSuspicious", Matchers.is(transferDto.getIsSuspicious())))
                .andExpect(jsonPath("$.blockedReason", Matchers.is(transferDto.getBlockedReason())))
                .andExpect(jsonPath("$.suspiciousReason", Matchers.is(transferDto.getSuspiciousReason())));
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        doReturn(transferDto).when(service).update(transferDto.getId(), transferDto);

        mockMvc.perform(put(path + "/" + transferDto.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(transferDto.getId().intValue())))
                .andExpect(jsonPath("$.cardTransferId",
                        Matchers.is(transferDto.getCardTransferId().intValue())))
                .andExpect(jsonPath("$.isBlocked", Matchers.is(transferDto.getIsBlocked())))
                .andExpect(jsonPath("$.isSuspicious", Matchers.is(transferDto.getIsSuspicious())))
                .andExpect(jsonPath("$.blockedReason", Matchers.is(transferDto.getBlockedReason())))
                .andExpect(jsonPath("$.suspiciousReason", Matchers.is(transferDto.getSuspiciousReason())));
    }
}