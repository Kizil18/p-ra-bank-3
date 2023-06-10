package com.bank.antifraud.controller;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.service.AuditService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.sql.Timestamp;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuditController.class)
class AuditControllerTest {
    private final static String PATH = "/audit/";
    private static final Long NONEXISTENT_ID = 555L;
    private AuditDto auditDto;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditService service;

    @BeforeEach
    void prepare() {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
        auditDto = new AuditDto(25L, "A", "B", "C", "D", createdAt,
                modifiedAt, "E", "F");

    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        doReturn(auditDto).when(service).findById(auditDto.getId());

        mockMvc.perform(get(PATH + auditDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(auditDto.getId().intValue())))
                .andExpect(jsonPath("$.entityType", Matchers.is(auditDto.getEntityType())))
                .andExpect(jsonPath("$.operationType", Matchers.is(auditDto.getOperationType())))
                .andExpect(jsonPath("$.createdBy", Matchers.is(auditDto.getCreatedBy())))
                .andExpect(jsonPath("$.modifiedBy", Matchers.is(auditDto.getModifiedBy())))
                .andExpect(jsonPath("$.createdAt", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.modifiedAt", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.newEntityJson", Matchers.is(auditDto.getNewEntityJson())))
                .andExpect(jsonPath("$.entityJson", Matchers.is(auditDto.getEntityJson())));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).findById(NONEXISTENT_ID);

        mockMvc.perform(get(PATH + NONEXISTENT_ID))
                .andExpect(status().isNotFound());
    }
}