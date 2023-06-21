package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.entity.PhoneTransferEntity;
import com.bank.transfer.service.PhoneTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для {@link PhoneTransferDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/phone")
@Tag(name = "Контроллер для переводов по номеру телефона")
public class PhoneTransferController {

    private final PhoneTransferService service;

    /**
     * @param ids список технических идентификаторов {@link PhoneTransferEntity}
     * @return {@link ResponseEntity} c листом {@link PhoneTransferDto}
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение всех переводов по ID")
    public ResponseEntity<List<PhoneTransferDto>> readAll(@RequestParam("id") List<Long> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }

    /**
     * @param id технический идентификатор {@link PhoneTransferEntity}
     * @return {@link ResponseEntity} {@link PhoneTransferDto}
     */
    @GetMapping("/read/{id}")
    @Operation(summary = "Получение перевода по ID")
    public PhoneTransferDto read(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    /**
     * @param phoneTransfer {@link PhoneTransferDto}
     * @return {@link ResponseEntity } {@link PhoneTransferDto}
     */
    @PostMapping("/create")
    @Operation(summary = "Создание нового перевода")
    public ResponseEntity<PhoneTransferDto> create(@RequestBody PhoneTransferDto phoneTransfer) {
        return ResponseEntity.ok(service.save(phoneTransfer));
    }

    /**
     * @param phoneTransfer {@link PhoneTransferDto}
     * @param id            технический идентификатор {@link PhoneTransferEntity}
     * @return {@link ResponseEntity} {@link PhoneTransferDto}
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление текущего перевода")
    public ResponseEntity<PhoneTransferDto> update(@PathVariable("id") Long id,
                                                   @RequestBody PhoneTransferDto phoneTransfer) {
        return ResponseEntity.ok(service.update(id, phoneTransfer));
    }
}
