package com.bank.transfer.controller;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.entity.CardTransferEntity;
import com.bank.transfer.service.CardTransferService;
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
 * Контроллер для {@link CardTransferDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
@Tag(name = "Контроллер для переводов по карте")
public class CardTransferController {

    private final CardTransferService service;

    /**
     * @param ids список технических идентификаторов {@link CardTransferEntity}
     * @return {@link ResponseEntity} c листом {@link CardTransferDto}
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение всех переводов по ID")
    public ResponseEntity<List<CardTransferDto>> readAll(@RequestParam("id") List<Long> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }

    /**
     * @param id технический идентификатор {@link CardTransferEntity}
     * @return {@link ResponseEntity} {@link CardTransferDto}
     */
    @GetMapping("/read/{id}")
    @Operation(summary = "Получение перевода по ID")
    public CardTransferDto read(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    /**
     * @param cardTransfer {@link CardTransferDto}
     * @return {@link ResponseEntity } {@link CardTransferDto}
     */
    @PostMapping("/create")
    @Operation(summary = "Создание нового перевода")
    public ResponseEntity<CardTransferDto> create(@RequestBody CardTransferDto cardTransfer) {
        return ResponseEntity.ok(service.save(cardTransfer));
    }

    /**
     * @param cardTransfer {@link CardTransferDto}
     * @param id           технический идентификатор {@link CardTransferEntity}
     * @return {@link ResponseEntity} {@link CardTransferDto}
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление текущего перевода")
    public ResponseEntity<CardTransferDto> update(@PathVariable("id") Long id,
                                                  @RequestBody CardTransferDto cardTransfer) {
        return ResponseEntity.ok(service.update(id, cardTransfer));
    }
}
