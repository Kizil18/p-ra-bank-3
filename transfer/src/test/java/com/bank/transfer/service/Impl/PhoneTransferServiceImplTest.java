package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.entity.PhoneTransferEntity;
import com.bank.transfer.mapper.PhoneTransferMapper;
import com.bank.transfer.repository.PhoneTransferRepository;

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
class PhoneTransferServiceImplTest {

    @Mock
    private PhoneTransferRepository repository;
    @Mock
    private PhoneTransferMapper mapper;
    @InjectMocks
    private PhoneTransferServiceImpl service;

    private PhoneTransferEntity entity;
    private PhoneTransferDto dto;

    @BeforeEach
    void setUp() {
        entity = new PhoneTransferEntity(1L, 88005553535L, BigDecimal.valueOf(1),
                "purpose", 1L);
        dto = new PhoneTransferDto(1L, 88005553535L, BigDecimal.valueOf(1),
                "purpose", 1L);
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        doReturn(Optional.of(entity)).when(repository).findById(anyLong());

        List<Long> list = List.of(1L, 2L);
        List<PhoneTransferDto> listDto = service.findAllById(list);

        System.out.println();
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        doReturn(Optional.of(entity)).when(repository).findById(any(Long.class));

        PhoneTransferDto transferDto = service.findById(entity.getId());

        assertEquals(1L, entity.getId());
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertThrows(NullPointerException.class, () -> service.findById(entity.getId()));
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        service.save(dto);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() {
        entity.setPurpose("new");
        service.save(mapper.toDto(entity));

        doReturn(Optional.of(entity)).when(repository).findById(any(Long.class));

        PhoneTransferDto result = service.update(1L, mapper.toDto(entity));

        Assertions.assertEquals(result, mapper.toDto(entity));
    }
}