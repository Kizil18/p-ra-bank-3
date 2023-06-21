package com.bank.account.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

class ExceptionReturnerTest {
    private ExceptionReturner exceptionReturner;

    @BeforeEach
    public void setUp() {
        exceptionReturner = new ExceptionReturner();
    }

    @Test
    @DisplayName("Тест метода getEntityNotFoundException")
    public void testGetEntityNotFoundException() {
        String message = "Сущность не найдена";
        EntityNotFoundException exception = exceptionReturner.getEntityNotFoundException(message);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(message, exception.getMessage());
    }
}