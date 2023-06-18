package com.bank.history.service;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.mapper.HistoryMapper;
import com.bank.history.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;
    @Captor
    private ArgumentCaptor<HistoryEntity> historyEntityArgumentCaptor;
    @InjectMocks
    private HistoryServiceImpl historyService;
    private HistoryEntity historyEntity1;
    private HistoryEntity historyEntity2;
    private HistoryDto historyDto1;
    private HistoryDto historyDto2;

    @BeforeEach
    public void init() {
        historyEntity1 = HistoryEntity.builder().id(123L).transferAuditId(1L).profileAuditId(2L).accountAuditId(3L)
                .antiFraudAuditId(4L).publicBankInfoAuditId(5L).authorizationAuditId(6L).build();
        historyEntity2 = HistoryEntity.builder().id(456L).transferAuditId(1L).profileAuditId(2L).accountAuditId(3L)
                .antiFraudAuditId(4L).publicBankInfoAuditId(5L).authorizationAuditId(6L).build();
        historyDto1 = HistoryDto.builder().id(123L).transferAuditId(1L).profileAuditId(2L).accountAuditId(3L)
                .antiFraudAuditId(4L).publicBankInfoAuditId(5L).authorizationAuditId(6L).build();
        historyDto2 = HistoryDto.builder().id(456L).transferAuditId(1L).profileAuditId(2L).accountAuditId(3L)
                .antiFraudAuditId(4L).publicBankInfoAuditId(5L).authorizationAuditId(6L).build();
    }

    @Test
    @DisplayName("Тест нахождения HistoryDto по id")
    void readByIdPositiveTest() {
        HistoryDto expectedHistoryDto = new HistoryDto
                (123L, 1L, 2L, 3L,
                        4L, 5L, 6L);

        when(historyRepository.findById(123L)).thenReturn(Optional.of(historyEntity1));
        when(historyMapper.toDto(Mockito.any(HistoryEntity.class))).thenReturn(expectedHistoryDto);

        HistoryDto actualHistoryDto = historyService.readById(123L);

        assertThat(actualHistoryDto).isEqualTo(expectedHistoryDto);
        assertThat(actualHistoryDto).isNotNull();
    }

    @Test
    @DisplayName("Негативный тест нахождения HistoryDto по id")
    public void readByIdNegativeTest() {
        doReturn(Optional.empty()).when(historyRepository).findById(anyLong());

        Optional<HistoryEntity> actualResult = historyRepository.findById(anyLong());

        assertThat(actualResult).isEmpty();
        Mockito.verifyNoInteractions(historyMapper);
    }

    @Test
    @DisplayName("Тест сохранения HistoryDto")
    void createPositiveTest() {
        when(historyMapper.toEntity(historyDto1)).thenReturn(historyEntity1);

        historyService.create(historyDto1);
        verify(historyRepository, Mockito.times(1))
                .save(historyEntityArgumentCaptor.capture());

        assertThat(historyEntityArgumentCaptor.getValue().getId()).isEqualTo(123L);
        assertThat(historyEntityArgumentCaptor.getValue().getTransferAuditId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Негативный тест сохранения HistoryDto")
    void createNegativeTest() {
        doReturn(Optional.empty()).when(historyRepository).save(any());

        assertThrows(Exception.class, () -> historyRepository.save(any()));
        verifyNoInteractions(historyMapper);
    }

    @Test
    @DisplayName("Тест обновления HistoryDto")
    void updatePositiveTest() {
        Long historyId = 123L;
        HistoryDto expectedHistoryDto = new HistoryDto
                (123L, 1L, 2L, 3L,
                        4L, 5L, 6L
                );

        when(historyRepository.findById(historyId)).thenReturn(Optional.of(historyEntity1));
        when(historyMapper.toDto(Mockito.any(HistoryEntity.class))).thenReturn(expectedHistoryDto);

        when(historyRepository.save(historyEntity1)).thenReturn(historyEntity1);
        when(historyMapper.mergeToEntity(historyDto1, historyEntity1)).thenReturn(historyEntity1);

        when(historyMapper.toDto(historyEntity1)).thenReturn(historyDto1);

        when(historyService.update(historyId, historyDto1)).thenReturn(historyDto1);

        HistoryDto returnDto = historyService.update(historyId, historyDto1);
        assertThat(returnDto).isNotNull();
    }

    @Test
    @DisplayName("Негативный тест обновления HistoryDto")
    void updateNegativeTest() {
        doReturn(Optional.empty()).when(historyRepository).save(any());
        doReturn(Optional.empty()).when(historyRepository).findById(anyLong());

        Optional<HistoryEntity> actualResult = historyRepository.findById(anyLong());

        assertThat(actualResult).isEmpty();
        assertThrows(Exception.class, () -> historyRepository.save(any()));
        verifyNoInteractions(historyMapper);
    }

    @Test
    @DisplayName("Тест нахождения списка HistoryDto по id")
    void readAllByIdPositiveTest() {
        List<HistoryDto> dtoList = List.of(historyDto1, historyDto2);
        List<HistoryEntity> entityList = List.of(historyEntity1, historyEntity2);
        List<Long> historyIdList = List.of(123L, 456L);

        when(historyRepository.findAllById(historyIdList)).thenReturn(entityList);
        when(historyMapper.toListDto(entityList)).thenReturn(dtoList);

        List<HistoryDto> returnList = historyService.readAllById(historyIdList);

        assertThat(returnList).isNotNull();
        assertThat(returnList).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("Негативный тест нахождения списка HistoryDto по id")
    void readAllByIdNegativeTest() {
        doReturn(null).when(historyRepository).findAllById(anyList());

        List<HistoryEntity> expectedList = historyRepository.findAllById(anyList());

        assertThat(expectedList).isEqualTo(null);
        verifyNoInteractions(historyMapper);
    }

}