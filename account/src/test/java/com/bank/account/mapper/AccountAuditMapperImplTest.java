package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

class AccountAuditMapperImplTest {
    private AccountAuditMapperImpl accountAuditMapper;
    private static final long ID = 1L;
    private static final Timestamp TIMESTAMP =
            new Timestamp(2023, 6, 1, 10, 00, 00, 00);

    @BeforeEach
    public void setUp() {
        accountAuditMapper = new AccountAuditMapperImpl();
    }

    @Test
    @DisplayName("Маппинг в дто")
    public void testToDto_PositiveScenario() {
        AuditEntity auditEntity = new AuditEntity(ID, "Entity Type",
                "Operation Type", "Create By", "Modified By",
                TIMESTAMP, TIMESTAMP, "New Entity Json", "Entity Json");

        AuditDto auditDto = accountAuditMapper.toDto(auditEntity);

        Assertions.assertNotNull(auditDto);
        Assertions.assertEquals(auditEntity.getId(), auditDto.getId());
        Assertions.assertEquals(auditEntity.getEntityType(), auditDto.getEntityType());
        Assertions.assertEquals(auditEntity.getOperationType(), auditDto.getOperationType());
        Assertions.assertEquals(auditEntity.getCreatedBy(), auditDto.getCreatedBy());
        Assertions.assertEquals(auditEntity.getModifiedBy(), auditDto.getModifiedBy());
        Assertions.assertEquals(auditEntity.getCreatedAt(), auditDto.getCreatedAt());
        Assertions.assertEquals(auditEntity.getModifiedAt(), auditDto.getModifiedAt());
        Assertions.assertEquals(auditEntity.getNewEntityJson(), auditDto.getNewEntityJson());
        Assertions.assertEquals(auditEntity.getEntityJson(), auditDto.getEntityJson());
    }

    @Test
    @DisplayName("Маппинг null в дто")
    public void toNullDtoTest() {
        AuditEntity auditEntity = null;

        AuditDto auditDto = accountAuditMapper.toDto(auditEntity);

        Assertions.assertNull(auditDto);
    }
}
