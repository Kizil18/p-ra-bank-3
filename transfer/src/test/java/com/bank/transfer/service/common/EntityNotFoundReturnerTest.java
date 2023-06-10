package com.bank.transfer.service.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EntityNotFoundReturnerTest {

    @Test
    void getEntityNotFoundException() {
        EntityNotFoundReturner notFoundReturner = new EntityNotFoundReturner();
        assertThat(notFoundReturner.getEntityNotFoundException(3L, "eeee"));
    }
}