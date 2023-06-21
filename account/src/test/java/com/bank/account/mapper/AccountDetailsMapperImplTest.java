package com.bank.account.mapper;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class AccountDetailsMapperImplTest {
    private AccountDetailsMapperImpl accountDetailsMapper;
    private static final Long ID = 1L;
    private static final Long ID2 = 2L;
    private AccountDetailsDto dto1;
    private List<AccountDetailsDto> accountDtos;
    private List<AccountDetailsEntity> accountEntitys;
    private AccountDetailsEntity entity1;
    private AccountDetailsEntity entity2;


    @BeforeEach
    public void setUp() {

        accountDetailsMapper = new AccountDetailsMapperImpl();
        dto1 = new AccountDetailsDto(ID, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        entity1 = new AccountDetailsEntity(ID, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        entity2 = new AccountDetailsEntity(ID2, 1L, 1L,
                1L, BigDecimal.ONE, true, 1L);
        accountEntitys = List.of(entity1, entity2);
    }

    @Test
    @DisplayName("маппинг в дто")
    public void testToDto_PositiveScenario() {
        dto1 = accountDetailsMapper.toDto(entity1);

        Assertions.assertNotNull(dto1);
        Assertions.assertEquals(entity1.getId(), dto1.getId());
        Assertions.assertEquals(entity1.getPassportId(), dto1.getPassportId());
        Assertions.assertEquals(entity1.getAccountNumber(), dto1.getAccountNumber());
        Assertions.assertEquals(entity1.getBankDetailsId(), dto1.getBankDetailsId());
        Assertions.assertEquals(entity1.getMoney(), dto1.getMoney());
        Assertions.assertEquals(entity1.getNegativeBalance(), dto1.getNegativeBalance());
        Assertions.assertEquals(entity1.getProfileId(), dto1.getProfileId());
    }

    @Test
    @DisplayName("Маппинг null в дто")
    public void testToDto_NegativeScenario() {
        dto1 = accountDetailsMapper.toDto(null);
        Assertions.assertNull(dto1);
    }

    @Test
    @DisplayName("Маппинг в лист DTO ")
    public void testToDtoList_PositiveScenario() {
        accountDtos = accountDetailsMapper.toDtoList(accountEntitys);

        Assertions.assertNotNull(accountDtos);
        Assertions.assertEquals(accountEntitys.size(), accountDtos.size());
    }

    @Test
    @DisplayName("Маппинг слияние")
    public void testMergeToEntity_PositiveScenario() {
        AccountDetailsEntity mergedEntity = accountDetailsMapper.mergeToEntity(entity2, dto1);

        Assertions.assertNotNull(mergedEntity);
        Assertions.assertEquals(dto1.getPassportId(), mergedEntity.getPassportId());
        Assertions.assertEquals(dto1.getAccountNumber(), mergedEntity.getAccountNumber());
        Assertions.assertEquals(dto1.getBankDetailsId(), mergedEntity.getBankDetailsId());
        Assertions.assertEquals(dto1.getMoney(), mergedEntity.getMoney());
        Assertions.assertEquals(dto1.getNegativeBalance(), mergedEntity.getNegativeBalance());
        Assertions.assertEquals(dto1.getProfileId(), mergedEntity.getProfileId());
    }
}
