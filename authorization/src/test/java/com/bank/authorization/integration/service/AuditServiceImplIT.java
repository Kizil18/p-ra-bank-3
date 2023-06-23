package com.bank.authorization.integration.service;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.integration.IntegrationTestBase;
import com.bank.authorization.service.AuditServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
class AuditServiceImplIT extends IntegrationTestBase {

    private final AuditServiceImpl auditService;

    private static final Long ID = 1L;


    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditDto auditDto = auditService.findById(ID);
        assertNotNull(auditDto);
        assertEquals(auditDto.getCreatedBy(), "test1");
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNotExistIdNegativeTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            auditService.findById(3L);
        });
    }
}