package com.bank.antifraud.integration.service.impl;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.integration.TestContainerConfiguration;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import com.bank.antifraud.service.impl.AuditServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AuditServiceImplIT extends TestContainerConfiguration {

    private static final Long NONEXISTENT_ID = 555L;
    private static final String MESSAGE = "Не найден аудит с ID  ";
    private AuditDto expected;
    private final AuditMapper mapper;
    private final AuditRepository repository;
    private final AuditServiceImpl service;

    @BeforeEach
    void prepare() {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
        AuditEntity audit = new AuditEntity(null, "A", "B", "C", "D", createdAt,
                modifiedAt, "E", "F");
        expected = mapper.toDto(repository.save(audit));
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditDto actual = service.findById(expected.getId());

        assertAll(
                () -> {
                    assertNotNull(actual);
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getEntityType(), actual.getEntityType());
                    assertEquals(expected.getOperationType(), actual.getOperationType());
                    assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
                    assertEquals(expected.getModifiedBy(), actual.getModifiedBy());
                    assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
                    assertEquals(expected.getModifiedAt(), actual.getModifiedAt());
                    assertEquals(expected.getNewEntityJson(), actual.getNewEntityJson());
                    assertEquals(expected.getEntityJson(), actual.getEntityJson());
                }
        );
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonexistentIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findById(NONEXISTENT_ID)
        );

        assertEquals(exception.getMessage(), MESSAGE + NONEXISTENT_ID);
    }
}

