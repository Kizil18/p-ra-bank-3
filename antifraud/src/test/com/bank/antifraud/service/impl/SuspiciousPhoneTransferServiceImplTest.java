package com.bank.antifraud.service.impl;

import com.bank.antifraud.common.data.SuspiciousPhoneTransferSupplier;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import com.bank.antifraud.mappers.SuspiciousPhoneTransferMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransferRepository;
import com.bank.antifraud.service.common.ExceptionReturner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuspiciousPhoneTransferServiceImplTest {
    private static final String MESSAGE = "SuspiciousPhoneTransfer по данному id не существует";
    private static final String ANY_MESSAGE = "Any message";
    private static final Long NONEXISTENT_ID = 555L;
    private SuspiciousPhoneTransferEntity transfer;
    private SuspiciousPhoneTransferEntity transfer2;
    private SuspiciousPhoneTransferDto transferDto;
    private List<SuspiciousPhoneTransferEntity> transfers;
    private List<SuspiciousPhoneTransferDto> transferDtos;
    private List<Long> transferIds;

    @Mock
    private ExceptionReturner returner;
    @Mock
    private SuspiciousPhoneTransferMapper mapper;
    @Mock
    private SuspiciousPhoneTransferRepository repository;
    @InjectMocks
    SuspiciousPhoneTransferServiceImpl service;

    @BeforeEach
    void prepare() {
        SuspiciousPhoneTransferSupplier supplier = new SuspiciousPhoneTransferSupplier();
        transfer = supplier.getTransferEntity(25L, 30L, false, true,
                "A", "B");
        transfer2 = supplier.getTransferEntity(35L, 40L, false, true,
                "A", "B");
        transferDto = supplier.getTransferDto(25L, 30L, false, true,
                "A", "B");
        SuspiciousPhoneTransferDto transferDto2 = supplier.getTransferDto(35L, 40L, false,
                true, "A", "B");
        transfers = supplier.getTransferEntities(transfer, transfer2);
        transferDtos = supplier.getTransferDtos(transferDto, transferDto2);
        transferIds = List.of(transfer.getId(), transfer2.getId());

        lenient().doThrow(new EntityNotFoundException(MESSAGE)).when(returner).getEntityNotFoundException(MESSAGE);
        lenient().doReturn(transfer).when(mapper).toEntity(transferDto);
        lenient().doReturn(transferDto).when(mapper).toDto(transfer);
        lenient().doReturn(Optional.of(transfer)).when(repository).findById(transfer.getId());
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        doReturn(transfer).when(repository).save(transfer);

        SuspiciousPhoneTransferDto actual = service.save(transferDto);

        assertAll(
                () -> verify(mapper).toEntity(transferDto),
                () -> verify(mapper).toDto(transfer),
                () -> verify(repository).save(transfer),
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
    @DisplayName("сохранение null, негативный сценарий")
    void saveNullEntityNegativeTest() {
        doReturn(null).when(mapper).toEntity(null);
        doThrow(new IllegalArgumentException(ANY_MESSAGE)).when(repository).save(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> service.save(null)
        );

        assertAll(
                () -> verify(repository).save(null),
                () -> verify(mapper).toEntity(null),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), ANY_MESSAGE)
        );
    }

    @Test
    @DisplayName("сохранение null в @NotNull-поле, негативный сценарий")
    void saveNullFieldEntityNegativeTest() {
        transferDto.setBlockedReason(null);
        transfer.setBlockedReason(null);
        doThrow(new DataIntegrityViolationException(ANY_MESSAGE)).when(repository).save(transfer);

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> service.save(transferDto)
        );

        assertAll(
                () -> verify(repository).save(transfer),
                () -> verify(mapper).toEntity(transferDto),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), ANY_MESSAGE)
        );
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousPhoneTransferDto actual = service.findById(transfer.getId());

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(mapper).toDto(transfer),
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

        assertAll(
                () -> verify(repository).findById(NONEXISTENT_ID),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }

    @Test
    @DisplayName("поиск по id равному null, негативный сценарий")
    void findByNullIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findById(null)
        );

        assertAll(
                () -> verify(repository).findById(null),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() {
        doReturn(transfer).when(mapper).mergeToEntity(transferDto, transfer);
        doReturn(transfer).when(repository).save(transfer);

        SuspiciousPhoneTransferDto actual = service.update(transferDto.getId(), transferDto);

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(mapper).mergeToEntity(transferDto, transfer),
                () -> verify(repository).save(transfer),
                () -> verify(mapper).toDto(transfer),
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
    @DisplayName("обновление null, негативный сценарий")
    void updateNullEntityNegativeTest() {
        doReturn(null).when(mapper).mergeToEntity(null, transfer);
        doThrow(new IllegalArgumentException(ANY_MESSAGE)).when(repository).save(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> service.update(transfer.getId(), null)
        );

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(mapper).mergeToEntity(null, transfer),
                () -> verify(repository).save(null),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), ANY_MESSAGE)
        );
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonexistentIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.update(NONEXISTENT_ID, any())
        );

        assertAll(
                () -> verify(repository).findById(NONEXISTENT_ID),
                () -> verify(mapper, never()).mergeToEntity(any(), any()),
                () -> verify(repository, never()).save(any()),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }

    @Test
    @DisplayName("обновление по id равному null, негативный сценарий")
    void updateByNullIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.update(null, any())
        );

        assertAll(
                () -> verify(repository).findById(null),
                () -> verify(mapper, never()).mergeToEntity(any(), any()),
                () -> verify(repository, never()).save(any()),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }

    @Test
    @DisplayName("поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        doReturn(Optional.of(transfer2)).when(repository).findById(transfer2.getId());
        doReturn(transferDtos).when(mapper).toListDto(transfers);

        List<SuspiciousPhoneTransferDto> actualList = service.findAllById(transferIds);

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(repository).findById(transfer2.getId()),
                () -> verify(mapper).toListDto(any()),
                () -> {
                    assertNotNull(actualList);
                    assertEquals(transferDtos, actualList);
                }
        );
    }

    @Test
    @DisplayName("поиск по нескольким id, по списку равному null, негативный сценарий")
    void findAllByNullIdListNegativeTest() {
        assertThrows(NullPointerException.class, () -> service.findAllById(null));

        assertAll(
                () -> verify(repository, never()).findById(any()),
                () -> verify(returner, never()).getEntityNotFoundException(any()),
                () -> verify(mapper, never()).toListDto(any())
        );
    }

    @Test
    @DisplayName("поиск по нескольким id, некоторые id несуществующие, негативный сценарий")
    void findAllByIdListWithNonexistentIdNegativeTest() {
        List<Long> idList = List.of(transfer.getId(), NONEXISTENT_ID);
        doReturn(Optional.empty()).when(repository).findById(NONEXISTENT_ID);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findAllById(idList)
        );

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(repository).findById(NONEXISTENT_ID),
                () -> verify(returner).getEntityNotFoundException(MESSAGE),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }

    @Test
    @DisplayName("поиск по нескольким id, некоторые id равны null, негативный сценарий")
    void findAllByIdListWithNullIdNegativeTest() {
        List<Long> idList = Stream.of(transfer.getId(), null).toList();
        doReturn(Optional.empty()).when(repository).findById(null);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> service.findAllById(idList)
        );

        assertAll(
                () -> verify(repository).findById(transfer.getId()),
                () -> verify(repository).findById(null),
                () -> verify(returner).getEntityNotFoundException(MESSAGE),
                () -> verify(mapper, never()).toDto(any()),
                () -> assertEquals(exception.getMessage(), MESSAGE)
        );
    }
}