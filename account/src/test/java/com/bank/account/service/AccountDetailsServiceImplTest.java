package com.bank.account.service;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import com.bank.account.service.common.ExceptionReturner;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceImplTest {
    private static final String MESSAGE_PREFIX = "Не существующий id = ";
    private static final Long ID = 1L;
    private static final Long ID2 = 2L;
    private AccountDetailsDto dto1;
    private AccountDetailsDto dto2;
    private List<AccountDetailsDto> accountDtos;
    private List<AccountDetailsEntity> accountEntitys;
    private AccountDetailsEntity entity1;
    private AccountDetailsEntity entity2;
    private List<Long> accountIds;
    @Mock
    private AccountDetailsMapper mapper;
    @Mock
    private AccountDetailsRepository repository;
    @Mock
    private ExceptionReturner exceptionReturner;
    @InjectMocks
    private AccountDetailsServiceImpl accountDetailsService;

    @BeforeEach
    void prepare() {
        dto1 = new AccountDetailsDto(ID, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        dto2 = new AccountDetailsDto(ID2, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        entity1 = new AccountDetailsEntity(ID, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        entity2 = new AccountDetailsEntity(ID2, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        accountIds = List.of(dto1.getId(), dto2.getId());
        accountDtos = List.of(dto1, dto2);
        accountEntitys = List.of(entity1, entity2);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void findByIdPositiveTest() {

        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(entity1));
        Mockito.when(mapper.toDto(Mockito.any(AccountDetailsEntity.class))).thenReturn(dto1);
        AccountDetailsDto actulResult = accountDetailsService.findById(entity1.getId());
        Assertions.assertThat(actulResult).isEqualTo(dto1);
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.when(repository.findById(ID)).thenReturn(Optional.empty());
        Mockito.when(exceptionReturner.getEntityNotFoundException(MESSAGE_PREFIX + ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> accountDetailsService.findById(ID));
    }

    @Test
    @DisplayName("Чтение по id равному null, негативный сценарий")
    void findByNullIdNegativeTest() {
        Mockito.when(repository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(exceptionReturner.getEntityNotFoundException(MESSAGE_PREFIX + null))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> accountDetailsService.findById(null));
    }

    @Test
    @DisplayName("Сохранение, позитивный сценарий")
    void savePositiveTest() {
        Mockito.doReturn(entity1).when(mapper).toEntity(dto1);
        Mockito.doReturn(entity1).when(repository).save(entity1);
        Mockito.doReturn(dto1).when(mapper).toDto(entity1);
        AccountDetailsDto actulResult = accountDetailsService.save(dto1);
        assertNotNull(actulResult);
        Assertions.assertThat(actulResult).isEqualTo(dto1);
        Mockito.verify(mapper).toEntity(dto1);
        Mockito.verify(repository).save(entity1);
        Mockito.verify(mapper).toDto(entity1);
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() {
        Mockito.doReturn(entity1).when(mapper).mergeToEntity(entity2, dto1);
        Mockito.doReturn(entity1).when(repository).save(entity1);
        Mockito.doReturn(dto1).when(mapper).toDto(entity1);
        Mockito.when(repository.findById(ID2)).thenReturn(Optional.of(entity2));
        AccountDetailsDto actulResult = accountDetailsService.update(ID2, dto1);
        assertNotNull(actulResult);
        Assertions.assertThat(actulResult).isEqualTo(dto1);
        Mockito.verify(mapper).mergeToEntity(entity2, dto1);
        Mockito.verify(repository).save(entity1);
        Mockito.verify(mapper).toDto(entity1);
        Mockito.verify(repository).findById(ID2);
    }

    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        Mockito.doReturn(Optional.of(entity1)).when(repository).findById(entity1.getId());
        Mockito.doReturn(Optional.of(entity2)).when(repository).findById(entity2.getId());
        Mockito.doReturn(accountDtos).when(mapper).toDtoList(accountEntitys);
        List<AccountDetailsDto> actualList = accountDetailsService.findAllById(accountIds);
        Mockito.verify(repository).findById(dto1.getId());
        Mockito.verify(repository).findById(dto2.getId());
        Mockito.verify(mapper).toDtoList(Mockito.any());
        assertNotNull(actualList);
        assertEquals(accountDtos, actualList);
    }
}