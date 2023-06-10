package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.repository.AccountTransferRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AccountTransferServiceImplTest {

    @Mock
    private AccountTransferRepository repository;
    @Mock
    private AccountTransferMapper mapper;
    @InjectMocks
    private AccountTransferServiceImpl accountTransferService;

    private AccountTransferEntity entity;
    private AccountTransferEntity secondEntity;
    private AccountTransferDto dto;

    @BeforeEach
    void setUp() {
        entity = new AccountTransferEntity(1L, 1L,
                BigDecimal.valueOf(1), "give", 1L);
        secondEntity = new AccountTransferEntity(2L, 1L,
                BigDecimal.valueOf(1), "give", 1L);
        dto = new AccountTransferDto(1L, 1L,
                BigDecimal.valueOf(1), "give", 1L);
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        doReturn(Optional.of(entity)).when(repository).findById(anyLong());

        List<Long> list = List.of(1L, 2L);
        List<AccountTransferDto> listDto = accountTransferService.findAllById(list);

        System.out.println();
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        doReturn(Optional.of(secondEntity)).when(repository).findById(any(Long.class));

        AccountTransferDto transferDto = accountTransferService.findById(secondEntity.getId());

        assertEquals(2L, secondEntity.getId());
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertThrows(NullPointerException.class, () -> accountTransferService.findById(entity.getId()));
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        accountTransferService.save(dto);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() {
        entity.setPurpose("new");
        accountTransferService.save(mapper.toDto(entity));

        doReturn(Optional.of(entity)).when(repository).findById(any(Long.class));

        AccountTransferDto result = accountTransferService.update(1L, mapper.toDto(entity));

        Assertions.assertEquals(result, mapper.toDto(entity));
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateNegativeTest() {
        assertThrows(NullPointerException.class, () -> accountTransferService.update(3L, dto));
    }
}