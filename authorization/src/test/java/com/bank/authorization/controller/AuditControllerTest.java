package com.bank.authorization.controller;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.service.AuditService;
import org.hamcrest.Matchers;
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

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuditController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuditControllerTest {
    private static final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditService auditService;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        AuditDto expectedDto = new AuditDto();
        expectedDto.setId(ID);
        when(auditService.findById(ID)).thenReturn(expectedDto);

        mockMvc.perform(get("/audit/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() throws Exception {
        when(auditService.findById(ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/read/1"))
                .andExpect(status().isNotFound());
    }
}