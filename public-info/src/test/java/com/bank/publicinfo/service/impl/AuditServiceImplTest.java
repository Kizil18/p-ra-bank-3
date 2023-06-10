package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {
    @InjectMocks
    private AuditServiceImpl auditService;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private AuditRepository auditRepository;
    private AuditDto auditDto;
    private AuditEntity auditEntity;
    private Timestamp timestamp;

    @BeforeEach
    public void init() {
        timestamp = new Timestamp(12L);

        auditDto = AuditDto.builder()
                .id(1L).entityType("audit").operationType("read")
                .createdBy("bank").modifiedBy("bank")
                .createdAt(timestamp).modifiedAt(timestamp)
                .newEntityJson("newEntity")
                .entityJson("Entity").build();
        auditEntity = AuditEntity.builder()
                .id(1L).entityType("audit").operationType("read")
                .createdBy("bank").modifiedBy("bank")
                .createdAt(timestamp).modifiedAt(timestamp)
                .newEntityJson("newEntity").entityJson("Entity")
                .build();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long auditId = 1L;
        AuditDto expectedAuditDto = auditDto;
        Mockito.when(auditRepository.findById(auditId)).thenReturn(Optional.of(auditEntity));
        Mockito.when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);
        AuditDto actualAudit = auditService.findById(auditId);
        Assertions.assertThat(actualAudit).isNotNull();
        Assertions.assertThat(actualAudit).isEqualTo(expectedAuditDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> auditService.findById(auditEntity.getId())
        );
        assertEquals("Не найден аудит с ID  " + auditEntity.getId(), exception.getMessage());
    }
}