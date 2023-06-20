package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.service.LicenseService;
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
 * Контроллер для {@link LicenseDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/license")
@Tag(name = "Контроллер для лицензий банка")
public class LicenseController {

    private final LicenseService service;

    /**
     * @param id технический идентификатор {@link LicenseEntity}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение сведений о лицензии банка по идентификатору")
    private ResponseEntity<LicenseDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link LicenseEntity}
     * @return {@link ResponseEntity}, лист {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение сведений о лицензиях банка по нескольким идентификаторам")
    private ResponseEntity<List<LicenseDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создание сведений о лицензии банка")
    private ResponseEntity<LicenseDto> create(@RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.create(license));
    }

    /**
     * @param id      технический идентификатор {@link LicenseEntity}
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление сведений о лицензии банка по идентификатору")
    private ResponseEntity<LicenseDto> update(@PathVariable("id") Long id,
                                              @RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.update(id, license));
    }
}
