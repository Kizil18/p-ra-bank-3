package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import com.bank.antifraud.service.SuspiciousPhoneTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для {@link SuspiciousPhoneTransferDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/suspicious/phone/transfer")
@Tag(name = "Контроллер для подозрительных переводов по номеру телефона")
public class SuspiciousPhoneTransferController {
    private final SuspiciousPhoneTransferService service;

    /**
     * @param id технический идентификатор {@link SuspiciousPhoneTransferEntity}
     * @return {@link ResponseEntity} {@link SuspiciousPhoneTransferDto}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение информации о подозрительном переводе по номеру телефона по id перевода")
    public ResponseEntity<SuspiciousPhoneTransferDto> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * @param ids список технических идентификаторов {@link SuspiciousPhoneTransferEntity}
     * @return {@link ResponseEntity} c листом {@link SuspiciousPhoneTransferDto}
     */
    @GetMapping
    @Operation(summary =
            "Получение информации о всех подозрительных переводах по номеру телефона из списка id переводов")
    public ResponseEntity<List<SuspiciousPhoneTransferDto>> readAll(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }

    /**
     * @param suspiciousTransfer {@link SuspiciousPhoneTransferDto}
     * @return {@link ResponseEntity} {@link SuspiciousPhoneTransferDto}
     */
    @PostMapping("/create")
    @Operation(summary = "Создание записи о подозрительном переводе по номеру телефона")
    public ResponseEntity<SuspiciousPhoneTransferDto> create(
            @Valid @RequestBody SuspiciousPhoneTransferDto suspiciousTransfer) {
        return ResponseEntity.ok(service.save(suspiciousTransfer));
    }

    /**
     * @param suspiciousTransfer {@link SuspiciousPhoneTransferDto}
     * @param id                 технический идентификатор {@link SuspiciousPhoneTransferEntity}
     * @return {@link ResponseEntity} {@link SuspiciousPhoneTransferDto}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновление данных о подозрительном переводе по номеру телефона по id перевода")
    public ResponseEntity<SuspiciousPhoneTransferDto> update(
            @Valid @RequestBody SuspiciousPhoneTransferDto suspiciousTransfer,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, suspiciousTransfer));
    }
}
