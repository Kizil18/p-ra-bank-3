package com.bank.antifraud.common.data;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuspiciousCardTransferSupplier {
    public SuspiciousCardTransferEntity getTransferEntity(Long id, Long cardTransferId,
                                                          Boolean isBlocked, Boolean isSuspicious,
                                                          String blockedReason, String suspiciousReason) {
        return new SuspiciousCardTransferEntity(id, cardTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousCardTransferEntity> getTransferEntities(SuspiciousCardTransferEntity... transfers) {
        final List<SuspiciousCardTransferEntity> result = new ArrayList<>();
        Collections.addAll(result, transfers);
        return result;
    }

    public SuspiciousCardTransferDto getTransferDto(Long id, Long cardTransferId,
                                                    Boolean isBlocked, Boolean isSuspicious,
                                                    String blockedReason, String suspiciousReason) {
        return new SuspiciousCardTransferDto(id, cardTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousCardTransferDto> getTransferDtos(SuspiciousCardTransferDto... transferDtos) {
        final List<SuspiciousCardTransferDto> result = new ArrayList<>();
        Collections.addAll(result, transferDtos);
        return result;
    }
}

