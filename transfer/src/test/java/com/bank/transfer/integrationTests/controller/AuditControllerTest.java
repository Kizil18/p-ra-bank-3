package com.bank.transfer.integrationTests.controller;

import com.bank.transfer.entity.AuditEntity;
import com.bank.transfer.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditRepository repository;

    @Container
    private final static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.hikari.schema", () -> "transfer");
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        AuditEntity entity = AuditEntity.builder().id(1L).entityType("me").operationType("me")
                .createdBy("me").modifiedBy("me").createdAt(Timestamp.valueOf("1970-01-01 01:01:01"))
                .modifiedAt(Timestamp.valueOf("1970-01-01 01:01:01")).newEntityJson("json")
                .entityJson("json").build();

        repository.save(entity);

        mockMvc.perform(get("/audit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() throws Exception {
        long entityID = 3L;

        AuditEntity entity = AuditEntity.builder().id(1L).entityType("me").operationType("me")
                .createdBy("me").modifiedBy("me").createdAt(Timestamp.valueOf("1970-01-01 01:01:01"))
                .modifiedAt(Timestamp.valueOf("1970-01-01 01:01:01")).newEntityJson("json")
                .entityJson("json").build();

        repository.save(entity);

        ResultActions response = mockMvc.perform(get("/audit/{id}", entityID));

        response.andExpect(status().isNotFound());

    }
}