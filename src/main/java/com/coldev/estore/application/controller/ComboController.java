package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.config.exception.mapper.ComboMapper;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.service.ComboService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/combos")
@Log4j2
public class ComboController {

    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;

    }

    @PostMapping
    public ResponseEntity<?> createCombo(@RequestBody ComboPostDto comboPostDto) {

        //TODO code to check token

        ComboGetDto comboGetDto = comboService.getComboById(
                comboService.createNewCombo(comboPostDto).getId(),
                ResponseLevel.ONE_INNER_LIST
        );

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.ACTION_SUCCESS)
                        .data(comboGetDto)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.DATA_FOUND)
                        .data(comboService.getComboById(id, ResponseLevel.ONE_INNER_LIST))
                        .build()
        );
    }
}