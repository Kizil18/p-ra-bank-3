package com.bank.antifraud.integration.controller;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.integration.TestContainerConfiguration;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
class AuditControllerIT extends TestContainerConfiguration {
    private static final Long NONEXISTENT_ID = 555L;
    private static final String PATH = "/audit/";
    private AuditEntity audit;
    private AuditDto expected;
    private final AuditMapper mapper;
    private final AuditRepository repository;
    private final MockMvc mockMvc;

    @BeforeEach
    void prepare() {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
        audit = new AuditEntity(null, "A", "B", "C", "D", createdAt,
                modifiedAt, "E", "F");
        expected = mapper.toDto(repository.save(audit));
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        mockMvc.perform(get(PATH + audit.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(expected.getId().intValue())))
                .andExpect(jsonPath("$.entityType", Matchers.is(expected.getEntityType())))
                .andExpect(jsonPath("$.operationType", Matchers.is(expected.getOperationType())))
                .andExpect(jsonPath("$.createdBy", Matchers.is(expected.getCreatedBy())))
                .andExpect(jsonPath("$.modifiedBy", Matchers.is(expected.getModifiedBy())))
                .andExpect(jsonPath("$.createdAt", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.modifiedAt", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.newEntityJson", Matchers.is(expected.getNewEntityJson())))
                .andExpect(jsonPath("$.entityJson", Matchers.is(expected.getEntityJson())));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        mockMvc.perform(get(PATH + NONEXISTENT_ID))
                .andExpect(status().isNotFound());
    }
}
