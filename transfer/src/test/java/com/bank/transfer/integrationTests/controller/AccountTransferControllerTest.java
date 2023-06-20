package com.bank.transfer.integrationTests.controller;

import com.bank.transfer.entity.AccountTransferEntity;
import com.bank.transfer.repository.AccountTransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class AccountTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountTransferRepository repository;

    @Autowired
    private ObjectMapper mapper;

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
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<AccountTransferEntity> entityList = List.of(AccountTransferEntity.builder().id(1L).accountNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build(),
                AccountTransferEntity.builder().id(2L).accountNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build());

        repository.saveAll(entityList);

        ResultActions response = mockMvc.perform(get("/account/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1, 2"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));
    }

    @Test
    @DisplayName("чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNotExistIdNegativeTest() throws Exception{
        List<Long> ids = List.of(3L, 4L, 5L, 6L);

        List<AccountTransferEntity> entityList = List.of(AccountTransferEntity.builder().id(1L).accountNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build(),
                AccountTransferEntity.builder().id(2L).accountNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build());
        repository.saveAll(entityList);

        ResultActions response = mockMvc.perform(get("/account/read/all", ids)
                .param("id", "3, 4"));

        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        AccountTransferEntity entity = AccountTransferEntity.builder().id(1L).accountNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        repository.save(entity);

        mockMvc.perform(get("/account/read/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNotExistIdNegativeTest() throws Exception {
        long entityID = 3L;

        AccountTransferEntity entity = AccountTransferEntity.builder().id(1L).accountNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        repository.save(entity);

        ResultActions response = mockMvc.perform(get("/account/read/{id}", entityID));

        response.andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        AccountTransferEntity entity = AccountTransferEntity.builder().id(1L).accountNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        ResultActions response = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(entity)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("создание пользователя с пустыми данными, негативный сценарий")
    void createNullPointerUserNegativeTest() throws Exception{
        ResultActions response = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(null)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception {
        AccountTransferEntity entity = AccountTransferEntity.builder().id(1L).accountNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        repository.save(entity);

        AccountTransferEntity updateEntity = AccountTransferEntity.builder().id(2L).accountNumber(3L)
                .amount(BigDecimal.valueOf(4)).purpose("give").accountDetailsId(5L).build();

        ResultActions response = mockMvc.perform(put("/account/update/1", entity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEntity)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный тест")
    void updateByAnotherIdNegativeTest() throws Exception {
        long entityID = 3L;

        AccountTransferEntity entity = AccountTransferEntity.builder().id(1L).accountNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();
        repository.save(entity);

        AccountTransferEntity updateEntity = AccountTransferEntity.builder().id(2L).accountNumber(3L)
                .amount(BigDecimal.valueOf(4)).purpose("give").accountDetailsId(5L).build();

        ResultActions response = mockMvc.perform(put("/account/update/{id}", entityID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEntity)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}