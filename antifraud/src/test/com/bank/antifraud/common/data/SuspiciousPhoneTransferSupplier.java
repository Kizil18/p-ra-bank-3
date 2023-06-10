package com.bank.antifraud.common.data;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuspiciousPhoneTransferSupplier {
    public SuspiciousPhoneTransferEntity getTransferEntity(Long id, Long phoneTransferId,
                                                           Boolean isBlocked, Boolean isSuspicious,
                                                           String blockedReason, String suspiciousReason) {
        return new SuspiciousPhoneTransferEntity(id, phoneTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousPhoneTransferEntity> getTransferEntities(SuspiciousPhoneTransferEntity... transfers) {
        final List<SuspiciousPhoneTransferEntity> result = new ArrayList<>();
        Collections.addAll(result, transfers);
        return result;
    }

    public SuspiciousPhoneTransferDto getTransferDto(Long id, Long phoneTransferId,
                                                     Boolean isBlocked, Boolean isSuspicious,
                                                     String blockedReason, String suspiciousReason) {
        return new SuspiciousPhoneTransferDto(id, phoneTransferId, isBlocked, isSuspicious,
                blockedReason, suspiciousReason);
    }

    public List<SuspiciousPhoneTransferDto> getTransferDtos(SuspiciousPhoneTransferDto... transferDtos) {
        final List<SuspiciousPhoneTransferDto> result = new ArrayList<>();
        Collections.addAll(result, transferDtos);
        return result;
    }
}
