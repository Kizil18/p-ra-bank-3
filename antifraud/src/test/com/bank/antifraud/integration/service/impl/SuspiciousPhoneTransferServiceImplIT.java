package com.bank.antifraud.integration.service.impl;

import com.bank.antifraud.common.data.SuspiciousPhoneTransferSupplier;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import com.bank.antifraud.integration.TestContainerConfiguration;
import com.bank.antifraud.mappers.SuspiciousPhoneTransferMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransferRepository;
import com.bank.antifraud.service.impl.SuspiciousPhoneTransferServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestConstructor;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SuspiciousPhoneTransferServiceImplIT extends TestContainerConfiguration {
    private static final String MESSAGE = "SuspiciousPhoneTransfer по данному id не существует";
    private static final Long NONEXISTENT_ID = 555L;
    private SuspiciousPhoneTransferEntity transfer2;
    private SuspiciousPhoneTransferDto transferDto;
    private SuspiciousPhoneTransferDto transferDto2;
    private List<SuspiciousPhoneTransferDto> transferDtos;
    private List<Long> transferIds;
    private final SuspiciousPhoneTransferMapper mapper;
    private final SuspiciousPhoneTransferRepository repository;
    private final SuspiciousPhoneTransferServiceImpl service;

    @BeforeEach
    void prepare() {
        SuspiciousPhoneTransferSupplier supplier = new SuspiciousPhoneTransferSupplier();
        SuspiciousPhoneTransferEntity transfer = supplier.getTransferEntity(null, 30L,
                false, true, "A", "B");
        transfer2 = supplier.getTransferEntity(null, 40L, false, true,
                "A", "B");
        transferDto = mapper.toDto(repository.save(transfer));
        transferDto2 = mapper.toDto(transfer2);
    }

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousPhoneTransferDto actual = service.save(transferDto2);

        SuspiciousPhoneTransferDto expected = mapper.toDto(repository.findById(actual.getId()).get());
        assertAll(
                () -> {
                    assertNotNull(actual);
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getPhoneTransferId(), actual.getPhoneTransferId());
                    assertEquals(expected.getIsBlocked(), actual.getIsBlocked());
                    assertEquals(expected.getIsSuspicious(), actual.getIsSuspicious());
                    assertEquals(expected.getBlockedReason(), actual.getBlockedReason());
                    assertEquals(expected.getSuspiciousReason(), actual.getSuspiciousReason());
                }
        );
    }

    @Test
    @DisplayName("сохранение null, негативный сценарий")
    void saveNullEntityNegativeTest() {
        assertThrows(
                InvalidDataAccessApiUsageException.class, () -> service.save(null)
        );
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousPhoneTransferDto actual = service.findById(transferDto.getId());

        assertAll(
                () -> {
                    assertNotNull(actual);
                    assertEquals(transferDto.getId(), actual.getId());
                    assertEquals(transferDto.getPhoneTransferId(), actual.getPhoneTransferId());
                    assertEquals(transferDto.getIsBlocked(), actual.getIsBlocked());
                    assertEquals(transferDto.getIsSuspicious(), actual.getIsSuspicious());
                    assertEquals(transferDto.getBlockedReason(), actual.getBlockedReason());
                    assertEquals(transferDto.getSuspiciousReason(), actual.getSuspiciousReason());
                }
        );
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonexistentIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findById(NONEXISTENT_ID)
        );

        assertEquals(exception.getMessage(), MESSAGE);
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() {
        transferDto.setBlockedReason("XXX");

        SuspiciousPhoneTransferDto actual = service.update(transferDto.getId(), transferDto);

        assertAll(
                () -> {
                    assertNotNull(actual);
                    assertEquals(transferDto.getId(), actual.getId());
                    assertEquals(transferDto.getPhoneTransferId(), actual.getPhoneTransferId());
                    assertEquals(transferDto.getIsBlocked(), actual.getIsBlocked());
                    assertEquals(transferDto.getIsSuspicious(), actual.getIsSuspicious());
                    assertEquals(transferDto.getBlockedReason(), actual.getBlockedReason());
                    assertEquals(transferDto.getSuspiciousReason(), actual.getSuspiciousReason());
                }
        );
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonexistentIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.update(NONEXISTENT_ID, transferDto)
        );

        assertEquals(exception.getMessage(), MESSAGE);
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        transferDto2 = mapper.toDto(repository.save(transfer2));
        transferDtos = List.of(transferDto, transferDto2);
        transferIds = List.of(transferDto.getId(), transferDto2.getId());

        List<SuspiciousPhoneTransferDto> actualList = service.findAllById(transferIds);

        assertAll(
                () -> {
                    assertNotNull(actualList);
                    assertEquals(transferDtos, actualList);
                }
        );
    }

    @Test
    @DisplayName("поиск по нескольким несуществующим id,  негативный сценарий")
    void findAllByIdListWithNonexistentIdNegativeTest() {
        transferIds = List.of(transferDto.getId(), NONEXISTENT_ID);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findAllById(transferIds)
        );

        assertEquals(exception.getMessage(), MESSAGE);
    }
}
