package com.bank.account.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuditDtoTest {
    private static final Timestamp TIMESTAMP =
            new Timestamp(2023, 6, 1, 10, 00, 00, 00);
    private AuditDto dto1;
    private AuditDto dto2;
    private AuditDto dto3;

    @BeforeEach
    void prepare() {
        dto1 = new AuditDto(1L, "test", "test",
                "test", "test", TIMESTAMP,
                TIMESTAMP, "test", "test");
        dto2 = new AuditDto(1L, "test", "test",
                "test", "test", TIMESTAMP,
                TIMESTAMP, "test", "test");
        dto3 = new AuditDto(2L, "test", "test",
                "test", "test", TIMESTAMP,
                TIMESTAMP, "test", "test");
    }

    @Test
    @DisplayName("Сравнение  Account Audit Dto ")
    void testEquals() {
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    @DisplayName("Hash Code  Account Audit Dto ")
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}