package com.bank.transfer.integrationTests.service;

import com.bank.transfer.entity.CardTransferEntity;
import com.bank.transfer.mapper.CardTransferMapper;
import com.bank.transfer.repository.CardTransferRepository;
import com.bank.transfer.service.Impl.CardTransferServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;

import java.math.BigDecimal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CardTransferServiceImplTest {

    @Autowired
    private CardTransferRepository repository;

    @Autowired
    private CardTransferMapper mapper;

    @Autowired
    private CardTransferServiceImpl service;

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
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<CardTransferEntity> entityList = List.of(CardTransferEntity.builder().id(1L).cardNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build(),
                CardTransferEntity.builder().id(2L).cardNumber(1L)
                        .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build());

        service.findById(1L);
    }

    @Test
    @DisplayName("поиск по нескольким id, негативный сценарий")
    void findAllByIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L, 3L, 4L);

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(ids));
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        CardTransferEntity entity = CardTransferEntity.builder().id(1L).cardNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        repository.save(entity);

        service.findById(entity.getId());
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        CardTransferEntity entity = CardTransferEntity.builder().id(1L).cardNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        repository.save(entity);

        assertThrows(EntityNotFoundException.class, () -> service.findById(2L));
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        CardTransferEntity entity = CardTransferEntity.builder().id(1L).cardNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        service.save(mapper.toDto(entity));
    }

    @Test
    @DisplayName("сохранение, негативный сценарий")
    void saveNegativeTest() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> service.save(null));
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() {
        CardTransferEntity entity = CardTransferEntity.builder().id(1L).cardNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        entity.setPurpose("new");

        repository.save(entity);

        service.update(1L, mapper.toDto(entity));
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateNegativeTest() {
        CardTransferEntity entity = CardTransferEntity.builder().id(1L).cardNumber(1L)
                .amount(BigDecimal.valueOf(1)).purpose("give").accountDetailsId(1L).build();

        entity.setPurpose("new");

        repository.save(entity);

        assertThrows(EntityNotFoundException.class, () -> service.update(3L, mapper.toDto(entity)));
    }
}