package com.bank.publicinfo.controller;

import com.bank.publicinfo.service.BranchService;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
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
 * Контроллер для {@link BranchDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
@Tag(name = "Контроллер для филиалов банка")
public class BranchController {

    private final BranchService service;

    /**
     * @param id технический идентификатор {@link BranchEntity}
     * @return {@link ResponseEntity}, {@link BankDetailsDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(summary = "Возвращает информацию о выбранном филиале банка", tags = "одна запись")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "данные о филиале банка успешно получена"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<BranchDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link BranchEntity}
     * @return {@link ResponseEntity}, лист {@link BranchDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Возвращает информацию о списке филиалов банка", tags = "список записей")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Данные о списке филиалов банка успешно получена"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<List<BranchDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создает новую запись о филиале банка", tags = "создание записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о филиале банка успешно создана"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<BranchDto> create(@RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.create(branch));
    }

    /**
     * @param id     технический идентификатор {@link BranchEntity}
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновляет информацию о филиале банка", tags = "обновление записи")
    @ApiResponses(value = {@ApiResponse(responseCode = "2xx",
                                        description = "Запись о филиале банка успешно обновлена"),
                           @ApiResponse(responseCode = "4xx",
                                        description = "Некорректный запрос")})
    private ResponseEntity<BranchDto> update(@PathVariable("id") Long id,
                                             @RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.update(id, branch));
    }
}
