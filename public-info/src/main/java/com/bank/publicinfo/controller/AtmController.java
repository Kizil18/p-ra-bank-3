package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.service.AtmService;
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
 * Контроллер для {@link AtmDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/atm")
@Tag(name = "Контроллер для банкоматов")
public class AtmController {

    private final AtmService service;

    /**
     * @param id технический идентификатор {@link AtmEntity}
     * @return {@link ResponseEntity}, {@link AtmDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение сведений о банкомате по идентификатору")
    private ResponseEntity<AtmDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link AtmEntity}
     * @return {@link ResponseEntity},лист {@link AtmDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение сведений о банкоматах по нескольким идентификаторам")
    private ResponseEntity<List<AtmDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param atm {@link AtmDto}
     * @return {@link ResponseEntity}, {@link AtmDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создание сведений о банкомате")
    private ResponseEntity<AtmDto> create(@RequestBody AtmDto atm) {
        return ResponseEntity.ok().body(service.create(atm));
    }

    /**
     * @param id  технический идентификатор {@link AtmEntity}
     * @param atm {@link AtmDto}
     * @return {@link ResponseEntity}, {@link AtmDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление сведений о банкомате по идентификатору")
    private ResponseEntity<AtmDto> update(@PathVariable("id") Long id,
                                          @RequestBody AtmDto atm) {
        return ResponseEntity.ok().body(service.update(id, atm));
    }
}
