package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
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
class BankDetailsServiceImplTest {
    @InjectMocks
    private BankDetailsServiceImpl bankDetailsService;
    @Mock
    private BankDetailsRepository bankDetailsRepository;
    @Mock
    private BankDetailsMapper detailsMapper;
    @Mock
    private EntityNotFoundSupplier entityNotFoundSupplier;
    private BankDetailsDto bankDetailsDto;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void init() {
        BigDecimal bigDecimal = new BigDecimal("30100000000000000001");
        bankDetailsEntity = BankDetailsEntity.builder()
                .id(1L).bik(44444444L)
                .inn(1111111111L).kpp(111111111L)
                .corAccount(bigDecimal)
                .city("Kazan").jointStockCompany("company")
                .name("name").build();
        bankDetailsDto = BankDetailsDto.builder()
                .id(1L).bik(44444444L)
                .inn(1111111111L).kpp(111111111L)
                .corAccount(bigDecimal)
                .city("Kazan").jointStockCompany("company")
                .name("name").build();
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L);
        List<BankDetailsEntity> entityList = List.of(bankDetailsEntity);
        List<BankDetailsDto> dtoList = List.of(bankDetailsDto);

        Mockito.when(bankDetailsRepository.findAllById(ids)).thenReturn(entityList);
        Mockito.when(detailsMapper.toDtoList(entityList)).thenReturn(dtoList);
        List<BankDetailsDto> actualDtoList = bankDetailsService.findAllById(ids);

        Assertions.assertThat(actualDtoList).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("поиск по некорректным id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(-1L, 2L);
        when(bankDetailsService.findAllById(ids)).thenThrow(new EntityNotFoundException("отрицательный id"));
        assertThat(assertThrows(EntityNotFoundException.class, () -> bankDetailsService.findAllById(ids)))
                .hasMessage("отрицательный id");
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() {
        BankDetailsDto expectedDto = bankDetailsDto;

        Mockito.when(bankDetailsRepository.save(bankDetailsEntity)).thenReturn(bankDetailsEntity);
        Mockito.when(detailsMapper.toDto(bankDetailsEntity)).thenReturn(expectedDto);
        Mockito.when(detailsMapper.toEntity(bankDetailsDto)).thenReturn(bankDetailsEntity);
        BankDetailsDto actualDto = bankDetailsService.create(bankDetailsDto);

        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("создание , негативный сценарий")
    void createByNonExistIdNegativeTest() {
        BankDetailsDto dto = new BankDetailsDto(null, 1L, 2L, 3L,
                new BigDecimal("30100000000000000001")
                , "c", "c", "name");
        when(bankDetailsService.create(dto)).thenThrow(new NullPointerException("null"));
        assertThat(assertThrows(NullPointerException.class, () -> bankDetailsService.create(dto)))
                .hasMessage("null");
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        BankDetailsDto expectedDto = bankDetailsDto;

        Mockito.when(bankDetailsRepository.findById(id)).thenReturn(Optional.of(bankDetailsEntity));
        Mockito.when(detailsMapper.mergeToEntity(bankDetailsDto, bankDetailsEntity)).thenReturn(bankDetailsEntity);
        Mockito.when(detailsMapper.toDto(bankDetailsEntity)).thenReturn(bankDetailsDto);
        BankDetailsDto actualDto = bankDetailsService.update(id, bankDetailsDto);

        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("обновление по несуществующему id,негативный сценраий")
    void updateByNonExistIdNegativeTest() {
        BankDetailsDto dto = new BankDetailsDto(null, 1L, 2L, 3L,
                new BigDecimal("30100000000000000001")
                , "c", "c", "name");
        when(entityNotFoundSupplier.getException("Информации о банке не найдено с id ", -1L))
                .thenThrow(new EntityNotFoundException("Информации о банке не найдено с id " + -1L));
        assertThat(assertThrows(EntityNotFoundException.class, () -> bankDetailsService.update(-1L, dto)))
                .hasMessage("Информации о банке не найдено с id " + -1L);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        BankDetailsDto expectedDto = bankDetailsDto;

        Mockito.when(bankDetailsRepository.findById(id)).thenReturn(Optional.of(bankDetailsEntity));
        Mockito.when(detailsMapper.toDto(bankDetailsEntity)).thenReturn(bankDetailsDto);
        BankDetailsDto actualDto = bankDetailsService.findById(id);
        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException entityNotFoundException =
                new EntityNotFoundException("Информации о банке не найдено с id " + 1L);
        Mockito.when(entityNotFoundSupplier.getException("Информации о банке не найдено с id ", 1L))
                .thenThrow(entityNotFoundException);

        Assertions.assertThat(assertThrows(EntityNotFoundException.class, () -> bankDetailsService.findById(1L)))
                .hasMessage("Информации о банке не найдено с id " + 1L);
    }


}