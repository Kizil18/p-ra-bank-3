package com.bank.history.integrationTest.service;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.repository.HistoryRepository;
import com.bank.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class HistoryServiceTest {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryService historyService;
    private HistoryDto historyDto1;
    @BeforeEach
    void setUp() {
        HistoryEntity historyEntity1 = new HistoryEntity(1L, 2L, 2L, 2L,
                2L, 2L, 2L);
        HistoryEntity historyEntity2 = new HistoryEntity(2L, 2L, 2L, 2L,
                2L, 2L, 2L);
        historyRepository.save(historyEntity1);
        historyRepository.save(historyEntity2);
        historyDto1 = new HistoryDto(3L, 5L, 5L,
                5L, 5L, 5L, 5L);
    }

    @Test
    @DisplayName("Тест нахождения HistoryDto по id, позитивный сценарий")
    void readByIdPositiveTest() {
        HistoryDto expectedHistoryDto = new HistoryDto
                (1L, 2L, 2L, 2L,
                        2L, 2L, 2L);

        HistoryDto actualHistoryDto = historyService.readById(1L);

        assertThat(actualHistoryDto.getAuthorizationAuditId()).isEqualTo(expectedHistoryDto.getAuthorizationAuditId());
        assertThat(actualHistoryDto).isNotNull();
    }

    @Test
    @DisplayName("Тест нахождения HistoryDto по id равному null, негативный сценарий")
    public void readByIdNegativeTest() {
        assertThrows(Exception.class, () -> historyService.readById(null));
    }

    @Test
    @DisplayName("Тест сохранения HistoryDto, позитивный сценарий")
    void createPositiveTest() {
        historyService.create(historyDto1);
        assertThat(historyRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Тест сохранения HistoryDto, негативный сценарий")
    void createNegativeTest() {
        assertThrows(Exception.class, () -> historyService.create(null));
    }

    @Test
    @DisplayName("Тест обновления HistoryDto, позитивный сценарий")
    void updatePositiveTest() {
        historyService.update(historyDto1.getId(), historyDto1);
        assertThat(historyRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Тест обновления HistoryDto, негативный сценарий")
    void updateNegativeTest() {
        assertThrows(Exception.class, () -> historyService.update(null, null));
    }

    @Test
    @DisplayName("Тест нахождения списка HistoryDto по id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        List<Long> historyIdList = List.of(1L, 2L);
        List<HistoryDto> returnList = historyService.readAllById(historyIdList);

        assertThat(returnList).isNotNull();
        assertThat(historyRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Тест нахождения списка HistoryDto по id равному null, негативный сценарий")
    void readAllByIdNegativeTest() {
        assertThrows(Exception.class, () -> historyRepository.findAllById(null));
    }

}
