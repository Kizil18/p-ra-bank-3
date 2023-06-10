package com.bank.transfer.controller;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.service.AuditService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.webjars.NotFoundException;

import java.sql.Timestamp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService service;

    private AuditDto dto;

    @BeforeEach
    void setUp() {
        dto = new AuditDto(1L, "me", "me", "me", "dsa",
                Timestamp.valueOf("1970-01-01 01:01:01"), Timestamp.valueOf("1970-01-01 01:01:01"),
                "json", "json");
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception{
        when(service.findById(dto.getId())).thenReturn(dto);

        mockMvc.perform(get("/audit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.entityType", Matchers.is("me")))
                .andExpect(jsonPath("$.operationType", Matchers.is("me")))
                .andExpect(jsonPath("$.createdBy", Matchers.is("me")))
                .andExpect(jsonPath("$.modifiedBy", Matchers.is("dsa")))
                .andExpect(jsonPath("$.newEntityJson", Matchers.is("json")))
                .andExpect(jsonPath("$.entityJson", Matchers.is("json")));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() {
        when(service.findById(2L)).thenThrow(new NotFoundException("Пользователя с таким ID не существует"));
    }
}