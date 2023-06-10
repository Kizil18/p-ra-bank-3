package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.entity.AuditEntity;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditMapper mapper;
    @Mock
    private AuditRepository repository;
    @InjectMocks
    private AuditServiceImpl service;

    private AuditEntity entity;

    @BeforeEach
    void setUp() {
        entity = new AuditEntity(1L, "me", "me", "me", "dsa",
                Timestamp.valueOf("1970-01-01 01:00:00"), Timestamp.valueOf("1970-01-01 01:00:00"),
                "json", "json");
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        doReturn(Optional.of(entity)).when(repository).findById(any(Long.class));

        AuditDto transferDto = service.findById(entity.getId());

        assertEquals(1L, entity.getId());
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(entity.getId()));
    }
}