package com.bank.authorization.service;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.AuditEntity;
import com.bank.authorization.mapper.AuditMapper;
import com.bank.authorization.repository.AuditRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {
    private static final Long ID = 1L;

    @Mock
    private AuditRepository repository;
    @Mock
    private AuditMapper mapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    void setup() {
//        repository = mock(AuditRepository.class);
//        mapper = mock(AuditMapper.class);
//        auditService = new AuditServiceImpl(repository, mapper);
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(ID);
        AuditDto expectedDto = new AuditDto();
        expectedDto.setId(ID);
        when(repository.findById(ID)).thenReturn(Optional.of(auditEntity));
        when(mapper.toDto(auditEntity)).thenReturn(expectedDto);

        AuditDto auditDto = auditService.findById(ID);

        assertNotNull(auditDto);
        assertEquals(expectedDto, auditDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNotExistIdNegativeTest() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            auditService.findById(ID);
        });
    }
}