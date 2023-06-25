package com.bank.antifraud.integration.controller;

import com.bank.antifraud.common.data.SuspiciousAccountTransferSupplier;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import com.bank.antifraud.integration.TestContainerConfiguration;
import com.bank.antifraud.mappers.SuspiciousAccountTransferMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransferRepository;
import com.bank.antifraud.service.impl.SuspiciousAccountTransferServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
class SuspiciousAccountTransferControllerIT extends TestContainerConfiguration {
    private static final Long NONEXISTENT_ID = 555L;
    private static final String PATH = "/suspicious/account/transfer";
    private SuspiciousAccountTransferEntity transfer2;
    private SuspiciousAccountTransferDto transferDto;
    private SuspiciousAccountTransferDto transferDto2;
    private final SuspiciousAccountTransferMapper mapper;
    private final ObjectMapper objectMapper;
    private final SuspiciousAccountTransferRepository repository;
    private final SuspiciousAccountTransferServiceImpl service;
    private final MockMvc mockMvc;

    @BeforeEach
    void prepare() {
        SuspiciousAccountTransferSupplier supplier = new SuspiciousAccountTransferSupplier();
        SuspiciousAccountTransferEntity transfer = supplier.getTransferEntity(null, 30L,
                false, true, "A", "B");
        transfer2 = supplier.getTransferEntity(null, 40L, false, true,
                "A", "B");
        transferDto = mapper.toDto(repository.save(transfer));
        transferDto2 = mapper.toDto(transfer2);
    }

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        SuspiciousAccountTransferDto expected = service.findById(transferDto.getId());

        mockMvc.perform(get(PATH + "/" + transferDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        mockMvc.perform(get(PATH + "/" + NONEXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        transferDto2 = mapper.toDto(repository.save(transfer2));
        List<Long> transferIds = List.of(transferDto.getId(), transferDto2.getId());

        List<SuspiciousAccountTransferDto> actualList = service.findAllById(transferIds);

        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", transferDto.getId().toString(), transferDto2.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(actualList)));
    }

    @Test
    @DisplayName("чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNotExistIdNegativeTest() throws Exception {
        mockMvc.perform(get(PATH)
                        .param("ids", NONEXISTENT_ID.toString(), NONEXISTENT_ID.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        mockMvc.perform(post(PATH + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.accountTransferId",
                        Matchers.is(transferDto2.getAccountTransferId().intValue())))
                .andExpect(jsonPath("$.isBlocked", Matchers.is(transferDto2.getIsBlocked())))
                .andExpect(jsonPath("$.isSuspicious", Matchers.is(transferDto2.getIsSuspicious())))
                .andExpect(jsonPath("$.blockedReason", Matchers.is(transferDto2.getBlockedReason())))
                .andExpect(jsonPath("$.suspiciousReason", Matchers.is(transferDto2.getSuspiciousReason())));
    }

    @Test
    @DisplayName("создание, негативный сценарий")
    void createNullNegativeTest() throws Exception {
        mockMvc.perform(post(PATH + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception {
        transferDto.setBlockedReason("XXX");

        mockMvc.perform(put(PATH + "/" + transferDto.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(transferDto)));
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNotExistIdNegativeTest() throws Exception {
        transferDto.setId(NONEXISTENT_ID);

        mockMvc.perform(put(PATH + "/" + transferDto.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isNotFound());
    }
}
