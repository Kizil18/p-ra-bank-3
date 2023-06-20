package com.bank.transfer.integrationTests.service;

import com.bank.transfer.entity.AuditEntity;
import com.bank.transfer.repository.AuditRepository;
import com.bank.transfer.service.Impl.AuditServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuditServiceImplTest {

    @Autowired
    private AuditRepository repository;

    @Autowired
    private AuditServiceImpl service;

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
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity entity = AuditEntity.builder().id(1L).entityType("me").operationType("me")
                .createdBy("me").modifiedBy("me").createdAt(Timestamp.valueOf("1970-01-01 01:01:01"))
                .modifiedAt(Timestamp.valueOf("1970-01-01 01:01:01")).newEntityJson("json")
                .entityJson("json").build();

        repository.save(entity);

        service.findById(entity.getId());

    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        AuditEntity entity = AuditEntity.builder().id(1L).entityType("me").operationType("me")
                .createdBy("me").modifiedBy("me").createdAt(Timestamp.valueOf("1970-01-01 01:01:01"))
                .modifiedAt(Timestamp.valueOf("1970-01-01 01:01:01")).newEntityJson("json")
                .entityJson("json").build();

        repository.save(entity);

        assertThrows(EntityNotFoundException.class, () -> service.findById(2L));
    }
}