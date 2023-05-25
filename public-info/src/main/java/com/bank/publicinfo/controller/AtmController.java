package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.service.AtmService;
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
    @Operation(summary = "Возвращает информацию о выбранном банкомате", tags = "одна запись")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Данные по банкомату найдены"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<AtmDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link AtmEntity}
     * @return {@link ResponseEntity},лист {@link AtmDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Возвращает информацию о нескольких банкоматах", tags = "список записей")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Данные по списку банкоматов найдены"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<List<AtmDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param atm {@link AtmDto}
     * @return {@link ResponseEntity}, {@link AtmDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создает новую запись о банкомате", tags = "создание записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о банкомате успешно создана"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<AtmDto> create(@RequestBody AtmDto atm) {
        return ResponseEntity.ok().body(service.create(atm));
    }

    /**
     * @param id  технический идентификатор {@link AtmEntity}
     * @param atm {@link AtmDto}
     * @return {@link ResponseEntity}, {@link AtmDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновляет информацию о банкомате", tags = "обновление записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о банкомате успешно обновлены"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<AtmDto> update(@PathVariable("id") Long id,
                                          @RequestBody AtmDto atm) {
        return ResponseEntity.ok().body(service.update(id, atm));
    }
}
