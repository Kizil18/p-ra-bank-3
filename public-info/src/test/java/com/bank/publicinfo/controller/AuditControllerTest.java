package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;
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

import java.sql.Timestamp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(controllers = AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuditControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditService auditService;
    @Autowired
    private ObjectMapper objectMapper;
    private AuditDto auditDto;
    private Timestamp timestamp;

    @BeforeEach
    void init() {
        timestamp = new Timestamp(12L);
        auditDto = AuditDto.builder()
                .id(1L).entityType("type")
                .operationType("operation")
                .createdBy("by").modifiedBy("mod")
                .createdAt(timestamp).modifiedAt(timestamp)
                .newEntityJson("new").entityJson("json").build();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readPositiveTest() throws Exception {
        Long id = 1L;
        when(auditService.findById(id)).thenReturn(auditDto);
        ResultActions response = mockMvc.perform(get("/audit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auditDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.entityType", Matchers.is("type")));
    }
    @Test
    @DisplayName("чтение по id, негативный сценарий")
    void readByNonExpectIdNegativeTest() throws Exception {
        when(auditService.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/audit/")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}