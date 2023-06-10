package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.CertificateEntity;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.CertificateRepository;
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
class CertificateServiceImplTest {
    @InjectMocks
    private CertificateServiceImpl service;
    @Mock
    private CertificateRepository repository;
    @Mock
    private CertificateMapper mapper;
    @Mock
    private EntityNotFoundSupplier supplier;
    private CertificateEntity entity;
    private CertificateDto dto;
    Byte[] byt = {0, 1, 2};
    BankDetailsEntity bankDetails = new BankDetailsEntity
            (1L, 2L, 3L, 4L, BigDecimal.TEN, "city", "company"
                    , "name");
    BankDetailsDto bankDetailsDto = new BankDetailsDto
            (1L, 2L, 3L, 4L, BigDecimal.TEN, "city", "company"
                    , "name");

    @BeforeEach
    void init() {
        entity = CertificateEntity.builder()
                .id(1L).photoCertificate(byt)
                .bankDetails(bankDetails).build();
        dto = CertificateDto.builder()
                .id(1L).photoCertificate(byt)
                .bankDetails(bankDetailsDto).build();
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L);
        List<CertificateEntity> entityList = List.of(entity);
        List<CertificateDto> dtoList = List.of(dto);
        Mockito.when(repository.findAllById(ids)).thenReturn(entityList);
        Mockito.when(mapper.toDtoList(entityList)).thenReturn(dtoList);
        List<CertificateDto> actualDto = service.findAllById(ids);
        Assertions.assertThat(actualDto).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("поиск по нескольким некорректным id, негативный сценарий")
    void findAllByIncorrectIdNegativeTest() {
        List<Long> ids = List.of(-1L, 2L);
        when(service.findAllById(ids)).thenThrow(new EntityNotFoundException("отрицательный id"));
        assertThat(assertThrows(EntityNotFoundException.class, () -> service.findAllById(ids)))
                .hasMessage("отрицательный id");
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() {
        CertificateDto expectedDto = new CertificateDto
                (1L, byt, bankDetailsDto);
        CertificateEntity certificateEntity = new CertificateEntity
                (1L, byt, bankDetails);
        Mockito.when(repository.save(entity)).thenReturn(certificateEntity);
        Mockito.when(mapper.toEntity(dto)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        CertificateDto actual = service.create(dto);
        Assertions.assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("создание по id равному null, негативный сценарий")
    void createByNonExistIdNegativeTest() {
        CertificateDto dto1 = new CertificateDto
                (null, byt, bankDetailsDto);
        when(service.create(dto)).thenThrow(new NullPointerException("null"));
        assertThat(assertThrows(NullPointerException.class, () -> service.create(dto1)))
                .hasMessage("null");
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        CertificateDto expectedDto = new CertificateDto
                (1L, byt, bankDetailsDto);
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.mergeToEntity(dto, entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        CertificateDto actual = service.update(id, dto);
        Assertions.assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        EntityNotFoundException exception = new EntityNotFoundException("Сертификата не найдено с id " + 1L);
        CertificateDto dto2 = new CertificateDto(1L, byt, bankDetailsDto);
        Mockito.when(supplier.getException("Сертификата не найдено с id ", 1L)).thenThrow(exception);

        assertThat(assertThrows(EntityNotFoundException.class, () -> service.update(1L, dto2)))
                .hasMessage("Сертификата не найдено с id " + 1L);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        CertificateDto expectedDto = new CertificateDto
                (1L, byt, bankDetailsDto);
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDto(entity)).thenReturn(dto);
        CertificateDto actualDto = service.findById(id);
        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException exception = new EntityNotFoundException("Сертификата не найдено с id " + 1L);
        Mockito.when(supplier.getException("Сертификата не найдено с id ", 1L)).thenThrow(exception);

        assertThat(assertThrows(EntityNotFoundException.class, () -> service.findById(1L)))
                .hasMessage("Сертификата не найдено с id " + 1L);
    }
}