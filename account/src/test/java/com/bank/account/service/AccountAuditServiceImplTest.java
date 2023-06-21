package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import org.assertj.core.api.Assertions;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AccountAuditMapper;
import com.bank.account.repository.AccountAuditRepository;
import com.bank.account.service.common.ExceptionReturner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AccountAuditServiceImplTest {
    private static final String MESSAGE = "Не существующий id = ";
    private static final long ID = 1L;
    private static final Timestamp TIMESTAMP =
            new Timestamp(2023, 6, 1, 10, 00, 00, 00);
    private AuditEntity audit;
    private AuditDto auditDto;
    @Mock
    private AccountAuditRepository repository;
    @Mock
    private AccountAuditMapper mapper;
    @Mock
    private ExceptionReturner exceptionReturner;
    @InjectMocks
    private AccountAuditServiceImpl accountAuditService;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        audit = createAudit(ID);
        auditDto = createAuditDto(ID);
        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(audit));
        Mockito.when(mapper.toDto(Mockito.any(AuditEntity.class))).thenReturn(auditDto);
        AuditDto actulResult = accountAuditService.findById(audit.getId());
        Assertions.assertThat(actulResult).isEqualTo(auditDto);
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.when(repository.findById(ID)).thenReturn(Optional.empty());
        Mockito.when(exceptionReturner.getEntityNotFoundException(MESSAGE + ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> accountAuditService.findById(ID));
    }

    @Test
    @DisplayName("Чтение по id равному null, негативный сценарий")
    void findByNullIdNegativeTest() {
        Mockito.when(repository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(exceptionReturner.getEntityNotFoundException(MESSAGE + null))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> accountAuditService.findById(null));
    }

    private static AuditDto createAuditDto(Long id) {
        return new AuditDto(id, "Entity Type", "Operation Type", "Create By",
                "Modified By", TIMESTAMP, TIMESTAMP, "New Entity Json", "Entity Json");
    }

    private static AuditEntity createAudit(Long id) {
        return new AuditEntity(id, "Entity Type", "Operation Type", "Create By",
                "Modified By", TIMESTAMP, TIMESTAMP, "New Entity Json", "Entity Json");
    }
}
