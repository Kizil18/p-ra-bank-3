package com.bank.antifraud.service.impl;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {
    private static final String MESSAGE = "Не найден аудит с ID  ";
    private static final Long NONEXISTENT_ID = 555L;
    private AuditEntity audit;
    private AuditDto auditDto;
    @Mock
    private AuditMapper mapper;
    @Mock
    private AuditRepository repository;
    @InjectMocks
    AuditServiceImpl service;

    @BeforeEach
    void prepare() {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
        audit = new AuditEntity(25L, "A", "B", "C", "D", createdAt,
                modifiedAt, "E", "F");
        auditDto = new AuditDto(25L, "A", "B", "C", "D", createdAt,
                modifiedAt, "E", "F");
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        doReturn(Optional.of(audit)).when(repository).findById(audit.getId());
        doReturn(auditDto).when(mapper).toDto(audit);

        AuditDto actual = service.findById(audit.getId());

        assertAll(
                ()-> {
                    assertNotNull(actual);
                    assertEquals(auditDto.getId(), actual.getId());
                    assertEquals(auditDto.getEntityType(), actual.getEntityType());
                    assertEquals(auditDto.getOperationType(), actual.getOperationType());
                    assertEquals(auditDto.getCreatedBy(), actual.getCreatedBy());
                    assertEquals(auditDto.getModifiedBy(), actual.getModifiedBy());
                    assertEquals(auditDto.getCreatedAt(), actual.getCreatedAt());
                    assertEquals(auditDto.getModifiedAt(), actual.getModifiedAt());
                    assertEquals(auditDto.getNewEntityJson(), actual.getNewEntityJson());
                    assertEquals(auditDto.getEntityJson(), actual.getEntityJson());
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

    @Test
    @DisplayName("чтение по id равному null, негативный сценарий")
    void findByNullIdNegativeTest() {

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findById(any())
        );

        assertEquals(exception.getMessage(), MESSAGE + "null");
    }
}