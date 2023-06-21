package com.bank.account.controller;

import com.bank.account.dto.AuditDto;
import com.bank.account.service.AccountAuditService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountAuditController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AccountAuditControllerTest {
    private static final Long AUDIT_ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountAuditService service;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void testRead() throws Exception {
        AuditDto expectedDto = new AuditDto(AUDIT_ID, "Entity Type", "Operation Type",
                "Created By", "Modified By", null, null,
                "New Entity Json", "Entity Json");

        Mockito.when(service.findById(AUDIT_ID)).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/audit/{id}", AUDIT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(AUDIT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entityType").value("Entity Type"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationType")
                        .value("Operation Type"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("Created By"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedBy").value("Modified By"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.newEntityJson")
                        .value("New Entity Json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entityJson").value("Entity Json"));
    }

    @Test
    @DisplayName("Чтение по id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        when(service.findById(AUDIT_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/audit/{id}", AUDIT_ID))
                .andExpect(status().isNotFound());
    }
}