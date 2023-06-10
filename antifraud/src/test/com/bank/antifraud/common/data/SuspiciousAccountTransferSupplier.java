package com.bank.antifraud.common.data;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuspiciousAccountTransferSupplier {
    public SuspiciousAccountTransferEntity getTransferEntity(Long id, Long accountTransferId,
                                                             Boolean isBlocked, Boolean isSuspicious,
                                                             String blockedReason, String suspiciousReason) {
        return new SuspiciousAccountTransferEntity(id, accountTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousAccountTransferEntity> getTransferEntities(SuspiciousAccountTransferEntity... transfers) {
        final List<SuspiciousAccountTransferEntity> result = new ArrayList<>();
        Collections.addAll(result, transfers);
        return result;
    }

    public SuspiciousAccountTransferDto getTransferDto(Long id, Long accountTransferId,
                                                       Boolean isBlocked, Boolean isSuspicious,
                                                       String blockedReason, String suspiciousReason) {
        return new SuspiciousAccountTransferDto(id, accountTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousAccountTransferDto> getTransferDtos(SuspiciousAccountTransferDto... transferDtos) {
        final List<SuspiciousAccountTransferDto> result = new ArrayList<>();
        Collections.addAll(result, transferDtos);
        return result;
    }
}
