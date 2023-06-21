package com.bank.account.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AccountDetailsDtoTest {
    private AccountDetailsDto dto1;
    private AccountDetailsDto dto2;
    private AccountDetailsDto dto3;

    @BeforeEach
    void prepare() {
        dto1 = new AccountDetailsDto(1L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        dto2 = new AccountDetailsDto(1L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        dto3 = new AccountDetailsDto(2L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
    }

    @Test
    @DisplayName("Сравнение Account Details Dto ")
    void testEquals() {
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    @DisplayName("Hash Code  Account Details Dto ")
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}