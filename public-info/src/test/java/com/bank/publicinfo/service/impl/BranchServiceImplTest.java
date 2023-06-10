package com.bank.publicinfo.service.impl;

 import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.BranchRepository;
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

 import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {
    @InjectMocks
    private BranchServiceImpl branchService;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BranchMapper mapper;
    @Mock
    private EntityNotFoundSupplier notFoundSupplier;
    private BranchDto branchDto;
    private BranchEntity branchEntity;

    @BeforeEach
    void init() {
        branchDto = BranchDto.builder()
                .id(1L).address("Moscow")
                .phoneNumber(12345L).city("Moscow")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN).build();
        branchEntity = BranchEntity.builder()
                .id(1L).address("Moscow")
                .phoneNumber(12345L).city("Moscow")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN).build();
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositive() {
        List<BranchDto> dtoList = List.of(branchDto);
        List<Long> ids = List.of(1L);
        List<BranchEntity> entityList = List.of(branchEntity);
        Mockito.when(branchRepository.findAllById(ids)).thenReturn(entityList);
        Mockito.when(mapper.toDtoList(entityList)).thenReturn(dtoList);
        List<BranchDto> actualDto = branchService.findAllById(ids);
        Assertions.assertThat(actualDto).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("поиск по некорректным id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(-1L, 2L);
        when(branchService.findAllById(ids)).thenThrow(new EntityNotFoundException("отрицательный id"));
        assertThat(assertThrows(EntityNotFoundException.class, () -> branchService.findAllById(ids)))
                .hasMessage("отрицательный id");
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() {
        BranchDto expectedDto = new BranchDto(1L, "Moscow", 12345L, "Moscow"
                , LocalTime.MIN, LocalTime.MIN);
        BranchEntity branchEntity1 = new BranchEntity(1L, "Moscow", 12345L, "Moscow"
                , LocalTime.MIN, LocalTime.MIN);
        Mockito.when(branchRepository.save(branchEntity)).thenReturn(branchEntity1);
        Mockito.when(mapper.toEntity(branchDto)).thenReturn(branchEntity);
        Mockito.when(mapper.toDto(branchEntity)).thenReturn(branchDto);
        BranchDto actualDto = branchService.create(branchDto);
        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("создание , негативный сценарий")
    void createByNonExistIdNegativeTest() {
        BranchDto dto = BranchDto.builder()
                .id(null).address("Moscow")
                .phoneNumber(12345L).city("Moscow")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN).build();
        when(branchService.create(dto)).thenThrow(new NullPointerException("null"));
        assertThat(assertThrows(NullPointerException.class, () -> branchService.create(dto)))
                .hasMessage("null");
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        BranchDto expectedDto = branchDto;
        Mockito.when(branchRepository.findById(id)).thenReturn(Optional.of(branchEntity));
        Mockito.when(mapper.mergeToEntity(branchDto, branchEntity)).thenReturn(branchEntity);
        Mockito.when(mapper.toDto(branchEntity)).thenReturn(branchDto);
        BranchDto actualDto = branchService.update(id, branchDto);
        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("обновление по несуществующему id,негативный сценраий")
    void updateByNonExistIdNegativeTest() {
        BranchDto dto = BranchDto.builder()
                .id(null).address("Moscow")
                .phoneNumber(12345L).city("Moscow")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN).build();
        when(notFoundSupplier.getException("Информации об отделении не найдено с id ", -1L))
                .thenThrow(new EntityNotFoundException("Информации об отделении не найдено с id " + -1L));
        assertThat(assertThrows(EntityNotFoundException.class, () -> branchService.update(-1L, dto)))
                .hasMessage("Информации об отделении не найдено с id " + -1L);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        BranchDto expectedDto = BranchDto.builder()
                .id(1L).address("Moscow")
                .phoneNumber(12345L).city("Moscow")
                .startOfWork(LocalTime.MIN)
                .endOfWork(LocalTime.MIN).build();
        Mockito.when(branchRepository.findById(id)).thenReturn(Optional.of(branchEntity));
        Mockito.when(mapper.toDto(branchEntity)).thenReturn(expectedDto);
        BranchDto actualDto = branchService.findById(id);
        Assertions.assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException entityNotFoundException =
                new EntityNotFoundException("Информации об отделении не найдено с id " + 1L);
        BranchDto dto = new BranchDto(1L, "moscow", 10L, "moscow"
                , LocalTime.MIN, LocalTime.MIN);
        Mockito.when(notFoundSupplier.getException("Информации об отделении не найдено с id ", 1L))
                .thenThrow(entityNotFoundException);

        assertThat(assertThrows(EntityNotFoundException.class, () -> branchService.update(1L, dto)))
                .hasMessage("Информации об отделении не найдено с id " + 1L);
    }
}