package com.bank.account.entity;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AccountDetailsEntityTest {
    private AccountDetailsEntity Entity1;
    private AccountDetailsEntity Entity2;
    private AccountDetailsEntity Entity3;

    @BeforeEach
    void prepare() {
        Entity1 = new AccountDetailsEntity(1L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        Entity2 = new AccountDetailsEntity(1L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        Entity3 = new AccountDetailsEntity(2L, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
    }

    @Test
    @DisplayName("Сравнение Account Entity ")
    void testEquals() {
        assertEquals(Entity1, Entity2);
        assertNotEquals(Entity1, Entity3);
    }

    @Test
    @DisplayName("Hash Code  Account Entity ")
    void testHashCode() {
        assertEquals(Entity1.hashCode(), Entity2.hashCode());
        assertNotEquals(Entity1.hashCode(), Entity3.hashCode());
    }
}