package com.bank.history.integrationTest.repository;

import com.bank.history.entity.HistoryEntity;
import com.bank.history.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository historyRepository;

    @BeforeEach
    void setUp() {
        HistoryEntity historyEntity1 = new HistoryEntity(1L, 2L, 2L, 2L,
                2L, 2L, 2L);
        HistoryEntity historyEntity2 = new HistoryEntity(2L, 5L, 5L, 5L,
                5L, 5L, 5L);
        historyRepository.save(historyEntity1);
        historyRepository.save(historyEntity2);
    }

    @Test
    @DisplayName("Тест нахождения по нескольким id, позитивный сценарий")
    public void findAllByIdPositiveTest() {
        List<Long> idList = List.of(1L, 2L);
        assertThat(historyRepository.findAllById(idList).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Тест нахождения по нескольким id равным null, негативный сценарий")
    public void findAllByIdNegativeTest() {
        assertThrows(Exception.class, () -> historyRepository.findAllById(null));
    }

    @Test
    @DisplayName("Тест нахождения по id, позитивный сценарий")
    public void findByIdPositiveTest() {
        Optional<HistoryEntity> expectedEntity = historyRepository.findById(1L);
        assertTrue(expectedEntity.isPresent());
    }

    @Test
    @DisplayName("Тест нахождения по id равному null, негативный сценарий")
    public void findByIdNegativeTest() {
        assertThrows(Exception.class, () -> historyRepository.findById(null));
    }

    @Test
    @DisplayName("Тест сохранения, позитивный сценарий")
    public void savePositiveTest() {
        HistoryEntity savedEntity = new HistoryEntity(3L, 3L, 3L,
                3L, 3L, 3L, 3L);
        historyRepository.save(savedEntity);
        assertThat(historyRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Тест сохранения null, негативный сценарий")
    public void saveNegativeTest() {
        assertThrows(Exception.class, () -> historyRepository.save(null));
    }

}
