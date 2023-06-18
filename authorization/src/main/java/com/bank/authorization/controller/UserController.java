package com.bank.authorization.controller;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import com.bank.authorization.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер {@link UserDto}
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер для аутентификации пользователей")
public class UserController {

    private final UserService service;

    /**
     * @param user заполненный полями role, profileId, password экземпляр DTO {@link UserDto}
     * @return {@link ResponseEntity}, {@link UserDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(summary = "Создание нового пользователя")
    public ResponseEntity<UserDto> create(@RequestBody UserDto user) {
        return new ResponseEntity<>(service.save(user), HttpStatus.CREATED);
    }

    /**
     * @param id технический идентификатор {@link UserEntity}
     * @return {@link ResponseEntity}, {@link UserDto} и HttpStatus.OK
     */
    @GetMapping("/read/{id}")
    @Operation(summary = "Получение информации о пользователе по ID")
    public ResponseEntity<UserDto> read(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    /**
     * @param user экземпляр DTO заполненный полями подлежащими изменению {@link UserDto}
     * @return {@link ResponseEntity}, {@link UserDto} и HttpStatus.OK
     */
    @PutMapping("/{id}/update")
    @Operation(summary = "Обновление информацию о пользователе по ID")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long id,
                                          @RequestBody UserDto user) {
        return new ResponseEntity<>(service.update(id, user), HttpStatus.OK);
    }

    /**
     * @param ids лист технических идентификаторов {@link UserEntity}
     * @return {@link ResponseEntity}, {@link UserDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(summary = "Получение информации обо всех пользователях")
    public ResponseEntity<List<UserDto>> readAll(@RequestParam List<Long> ids) {
        return new ResponseEntity<>(service.findAllByIds(ids), HttpStatus.OK);
    }
}
