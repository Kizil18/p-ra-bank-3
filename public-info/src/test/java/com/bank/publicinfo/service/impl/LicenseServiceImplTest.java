package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.LicenseRepository;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenseServiceImplTest {
    @InjectMocks
    private LicenseServiceImpl service;
    @Mock
    private LicenseRepository repository;
    @Mock
    private LicenseMapper mapper;
    @Mock
    private EntityNotFoundSupplier supplier;
    private LicenseEntity entity;
    private LicenseDto dto;
    Byte[] byt = {0, 1, 2};
    BankDetailsEntity bankDetails = new BankDetailsEntity
            (1L, 2L, 3L, 4L, BigDecimal.TEN, "city", "company"
                    , "name");
    BankDetailsDto bankDetailsDto = new BankDetailsDto
            (1L, 2L, 3L, 4L, BigDecimal.TEN, "city", "company"
                    , "name");

    @BeforeEach
    void init() {
        entity = LicenseEntity.builder()
                .id(1L).photoLicense(byt)
                .bankDetails(bankDetails).build();
        dto = LicenseDto.builder()
                .id(1L).photoLicense(byt)
                .bankDetails(bankDetailsDto).build();
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L);
        List<LicenseEntity> entityList = List.of(entity);
        List<LicenseDto> dtoList = List.of(dto);
        Mockito.when(repository.findAllById(ids)).thenReturn(entityList);
        Mockito.when(mapper.toDtoList(entityList)).thenReturn(dtoList);
        List<LicenseDto> actualDto = service.findAllById(ids);
        Assertions.assertThat(actualDto).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("поиск по некорректным id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(-1L, 2L);
        when(service.findAllById(ids)).thenThrow(new EntityNotFoundException("отрицательный id"));
        assertThat(assertThrows(EntityNotFoundException.class, () -> service.findAllById(ids)))
                .hasMessage("отрицательный id");
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() {
        LicenseDto expectedDto = new LicenseDto
                (1L, byt, bankDetailsDto);
        LicenseEntity licenseEntity = new LicenseEntity
                (1L, byt, bankDetails);
        Mockito.when(repository.save(entity)).thenReturn(licenseEntity);
        Mockito.when(mapper.toEntity(dto)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        LicenseDto actual = service.create(dto);
        Assertions.assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("создание , негативный сценарий")
    void createByNonExistIdNegativeTest() {
        LicenseDto dto2 = new LicenseDto(null, byt, bankDetailsDto);
        when(service.create(dto)).thenThrow(new NullPointerException("null"));
        assertThat(assertThrows(NullPointerException.class, () -> service.create(dto2)))
                .hasMessage("null");
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        LicenseDto licenseDto = new LicenseDto
                (1L, byt, bankDetailsDto);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.mergeToEntity(dto, entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        LicenseDto update = service.update(id, dto);
        Assertions.assertThat(update).isEqualTo(licenseDto);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        EntityNotFoundException exception = new EntityNotFoundException("Лицензии не найдено с id " + -1L);
        LicenseDto dto2 = new LicenseDto(1L, byt, bankDetailsDto);
        Mockito.when(supplier.getException("Лицензии не найдено с id ", -1L)).thenThrow(exception);

        assertThat(assertThrows(EntityNotFoundException.class, () -> service.update(-1L, dto2)))
                .hasMessage("Лицензии не найдено с id " + -1L);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        LicenseDto licenseDto = new LicenseDto
                (1L, byt, bankDetailsDto);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        LicenseDto byId = service.findById(id);
        Assertions.assertThat(byId).isEqualTo(licenseDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException exception = new EntityNotFoundException("Лицензии не найдено с id " + 1L);
        Mockito.when(supplier.getException("Лицензии не найдено с id ", 1L)).thenThrow(exception);

        assertThat(assertThrows(EntityNotFoundException.class, () -> service.findById(1L)))
                .hasMessage("Лицензии не найдено с id " + 1L);
    }
}