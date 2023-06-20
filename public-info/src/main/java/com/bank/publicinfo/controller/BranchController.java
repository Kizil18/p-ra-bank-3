package com.bank.publicinfo.controller;

import com.bank.publicinfo.service.BranchService;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
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
 * Контроллер для {@link BranchDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
@Tag(name = "Контроллер для отделений банка")
public class BranchController {

    private final BranchService service;

    /**
     * @param id технический идентификатор {@link BranchEntity}
     * @return {@link ResponseEntity}, {@link BankDetailsDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение сведений об отделении банка по идентификатору")
    private ResponseEntity<BranchDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link BranchEntity}
     * @return {@link ResponseEntity}, лист {@link BranchDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение сведений об отделениях банка по нескольким идентификаторам")
    private ResponseEntity<List<BranchDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создание сведений об отделении банка")
    private ResponseEntity<BranchDto> create(@RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.create(branch));
    }

    /**
     * @param id     технический идентификатор {@link BranchEntity}
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление сведений об отделении банка по идентификатору")
    private ResponseEntity<BranchDto> update(@PathVariable("id") Long id,
                                             @RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.update(id, branch));
    }
}
