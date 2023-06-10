package com.bank.publicinfo.util;

import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EntityNotFoundSupplierTest {
    @Spy
    @InjectMocks
    private EntityNotFoundSupplier entityNotFoundSupplier;
    @Mock
    private EntityNotFoundException entityNotFoundException;
    private AtmEntity atmEntity;

    @BeforeEach
    void init() {
        entityNotFoundException = new EntityNotFoundException(1L + "error");
        atmEntity = AtmEntity.builder().id(1L).address("Riga")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN)
                .allHours(true).branch(new BranchEntity(1L, "One", 2L, "Two", LocalTime.MIN,
                        LocalTime.MIN))
                .build();
    }

    @Test
    @DisplayName("получение EntityNotFoundException, позитивный сценарий")
    public void getExceptionPositiveTest() {
        Long id = 1L;
        Mockito.when(entityNotFoundSupplier.getException("error", id)).thenReturn(entityNotFoundException);
        EntityNotFoundException error = entityNotFoundSupplier.getException("error", 1L);
        Assertions.assertThat(error).isEqualTo(entityNotFoundException);
    }

    @Test
    @DisplayName("сравнение количества id с количеством entity, позитивный сценарий")
    void checkForSizeAndLoggingPositiveTest() {
        String message = "error";
        List<Long> ids = List.of(1L, 1L, 2L);
        List<AtmEntity> atmEntityList = List.of(atmEntity, atmEntity);
        assertThrows(EntityNotFoundException.class, () ->
                entityNotFoundSupplier.checkForSizeAndLogging(message, ids, atmEntityList));
    }
}