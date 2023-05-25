package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.service.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Контроллер для лицензий")
public class LicenseController {

    private final LicenseService service;

    /**
     * @param id технический идентификатор {@link LicenseEntity}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(summary = "Возвращает информацию о выбранной лицензии", tags = "одна запись")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Данные о лицензии успешно получены"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<LicenseDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link LicenseEntity}
     * @return {@link ResponseEntity}, лист {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Возвращает информацию о списке лицензий", tags = "список записей")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Данные о списке лицензий успешно получены"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<List<LicenseDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создает новую запись о лицензии", tags = "создание записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о лицензии успешно создана"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<LicenseDto> create(@RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.create(license));
    }

    /**
     * @param id      технический идентификатор {@link LicenseEntity}
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновляет информацию о выбранной лицензии", tags = "обновление записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о лицензии успешно обновлена"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<LicenseDto> update(@PathVariable("id") Long id,
                                              @RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.update(id, license));
    }
}
