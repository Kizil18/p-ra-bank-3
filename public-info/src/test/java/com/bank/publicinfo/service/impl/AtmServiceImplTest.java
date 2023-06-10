package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtmServiceImplTest {
    @Spy
    @InjectMocks
    private AtmServiceImpl atmService;
    @Mock
    private AtmRepository atmRepository;
    @Mock
    private AtmMapper atmMapper;
    @Mock
    private EntityNotFoundSupplier foundSupplier;
    @Mock
    private EntityNotFoundException entityNotFoundException;
    @Captor
    ArgumentCaptor<AtmEntity> argumentCaptor;
    private LocalTime localTime;

    private AtmEntity atmEntity;
    private AtmDto atmDto;

    @BeforeEach
    public void init() {
        localTime = LocalTime.of(10, 20, 30, 123456789);
        atmEntity = AtmEntity.builder().id(1L).address("Riga")
                .startOfWork(localTime)
                .endOfWork(localTime)
                .allHours(true)
                .branch(new BranchEntity(1L, "One", 2L, "Two", localTime, localTime))
                .build();
        atmDto = AtmDto.builder().id(1L).address("Riga")
                .startOfWork(localTime)
                .endOfWork(localTime)
                .allHours(true)
                .branch(new BranchDto(1L, "One", 2L, "Two", localTime, localTime))
                .build();
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        Long atmId = 1L;
        List<Long> ids = new ArrayList<>();
        ids.add(atmId);
        List<AtmDto> expectedAtmDtoList = new ArrayList<>();
        expectedAtmDtoList.add(atmDto);
        List<AtmEntity> atmEntityList = new ArrayList<>();
        atmEntityList.add(atmEntity);
        when(atmRepository.findAllById(ids)).thenReturn(atmEntityList);
        when(atmMapper.toDtoList(atmEntityList)).thenReturn(expectedAtmDtoList);
        List<AtmDto> returnAtmDto = atmService.findAllById(ids);
        Assertions.assertThat(returnAtmDto).isNotNull();
        Assertions.assertThat(returnAtmDto).isEqualTo(expectedAtmDtoList);
    }

    @Test
    @DisplayName("поиск по нескольким некорректным id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(-1L, 2L);
        when(atmService.findAllById(ids)).thenThrow(new EntityNotFoundException("отрицательный id"));
        assertThat(assertThrows(EntityNotFoundException.class, () -> atmService.findAllById(ids)))
                .hasMessage("отрицательный id");
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() {
        when(atmRepository.save(atmEntity)).thenReturn(atmEntity);
        when(atmMapper.toEntity(atmDto)).thenReturn(atmEntity);
        atmService.create(atmDto);
        Mockito.verify(atmRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue().getId()).isEqualTo(atmDto.getId());
        Assertions.assertThat(argumentCaptor.getValue().getAddress()).isEqualTo(atmDto.getAddress());
    }

    @Test
    @DisplayName("создание, негативный сценарий")
    void createByNonExistIdNegativeTest() {
        AtmDto dto = new AtmDto(null, "a", LocalTime.MIN, LocalTime.MIN
                , true, new BranchDto(1L, "a", 1L, "c", LocalTime.MIN
                , LocalTime.MIN));
        when(atmService.create(dto)).thenThrow(new NullPointerException("null"));
        assertThat(assertThrows(NullPointerException.class, () -> atmService.create(dto)))
                .hasMessage("null");

    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        Long atmId = 1L;
        AtmDto expectedAtmDto = new AtmDto
                (1L, "Riga", localTime, localTime, true
                        , new BranchDto(1L, "One", 2L, "Two", localTime, localTime));

        when(atmRepository.findById(atmId)).thenReturn(Optional.of(atmEntity));
        when(atmMapper.mergeToEntity(atmDto, atmEntity)).thenReturn(atmEntity);
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);
        when(atmService.update(atmId, expectedAtmDto)).thenReturn(atmDto);
        AtmDto actualAtmDto = atmService.update(atmId, atmDto);
        Assertions.assertThat(actualAtmDto).isEqualTo(expectedAtmDto);
    }

    @Test
    @DisplayName("обновление по несуществующему id,негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        entityNotFoundException = new EntityNotFoundException("Банкомат не найден с id " + 1L);
        AtmDto atmDto1 = new AtmDto(1L, "address", LocalTime.MIN, LocalTime.MIN, true
                , new BranchDto
                (1L, "address", 891L, "address", LocalTime.MIN, LocalTime.MIN));
        when(foundSupplier.getException("Банкомат не найден с id ", 1L)).thenThrow(entityNotFoundException);
        assertThat(assertThrows(EntityNotFoundException.class, () -> atmService.update(1L, atmDto1)))
                .hasMessage("Банкомат не найден с id " + 1L);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long atmId = 1L;
        AtmDto expectedAtmDto = new AtmDto
                (1L, "Riga", localTime, localTime, true
                        , new BranchDto(1L, "One", 2L, "Two", localTime, localTime));


        when(atmRepository.findById(atmId)).thenReturn(Optional.of(atmEntity));
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);
        when(atmService.findById(atmId)).thenReturn(atmDto);
        AtmDto actualAtmDto = atmService.findById(atmId);
        Assertions.assertThat(actualAtmDto).isEqualTo(expectedAtmDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        entityNotFoundException = new EntityNotFoundException("Банкомат не найден с id " + 1L);
        AtmDto atmDto1 = new AtmDto(1L, "address", LocalTime.MIN, LocalTime.MIN, true
                , new BranchDto
                (1L, "address", 891L, "address", LocalTime.MIN, LocalTime.MIN));
        when(foundSupplier.getException("Банкомат не найден с id ", 1L)).thenThrow(entityNotFoundException);

        assertThat(assertThrows(EntityNotFoundException.class, () -> atmService.findById(1L)))
                .hasMessage("Банкомат не найден с id " + 1L);
    }
}