package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.enumerate.SortType;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.combo.request.ComboFilterRequest;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.service.ComboService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/v1/combos")
@Log4j2
public class ComboController {

    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.DATA_FOUND)
                        .data(comboService.getComboDtoById(id, ResponseLevel.ONE_LEVEL_DEPTH))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> search
            (@RequestParam(name = "page", defaultValue = "0") int page,
             @RequestParam(name = "size", defaultValue = "100") int size,
             @RequestParam(name = "sort_by", required = false) String sortBy,
             @RequestParam(name = "sort_type", required = false) SortType sortType,

             @RequestParam(name = "name_contains", required = false) String searchKey,
             @RequestParam(name = "description_contains", required = false) String searchDescription,

             @RequestParam(name = "discount_percent_min", required = false) Double discountPercentMin,
             @RequestParam(name = "discount_percent_max", required = false) Double discountPercentMax,

             @RequestParam(name = "discount_value_min", required = false) BigDecimal discountValueMin,
             @RequestParam(name = "discount_value_max", required = false) BigDecimal discountValueMax,

             @RequestParam(name = "status", required = false) Status status,

             @RequestParam(name = "by_product_id", required = false) Long byProductId,
             @RequestParam(name = "by_product_count", required = false) Long byProductCount
            ) {

        ComboFilterRequest comboFilterRequest = ComboFilterRequest.builder()
                .pageNo(page).pageSize(size)
                .sortOrder(sortType).sortAttribute(sortBy)
                .searchKey(searchKey).descriptionContains(searchDescription)
                .discountPercentMin(discountPercentMin)
                .discountPercentMax(discountPercentMax)
                .discountValueMin(discountValueMin)
                .discountValueMax(discountValueMax)
                .status(status)
                .byProductId(byProductId)
                .byProductCount(byProductCount)
                .build();

        List<ComboGetDto> comboGetDtoList = comboService.getComboDtoList(
                comboFilterRequest, ResponseLevel.BASIC
        );

        ResponseObject.ResponseObjectBuilder<List<ComboGetDto>> responseBuilder =
                ResponseObject.builder();

        if (!comboGetDtoList.isEmpty()) {
            ResponseObject<List<ComboGetDto>> response = responseBuilder
                    .message(MessageDictionary.DATA_FOUND)
                    .data(comboGetDtoList)
                    .totalItems(comboGetDtoList.size()).build();

            return ResponseEntity.ok(response);
        } else {
            ResponseObject<?> response = responseBuilder.message(MessageDictionary.DATA_NOT_FOUND)
                    .totalItems(0).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


    }

    @PostMapping
    public ResponseEntity<?> createCombo(@RequestBody ComboPostDto comboPostDto) {

        //TODO code to check token
        ComboGetDto comboGetDto = comboService.getComboDtoById(
                comboService.createNewCombo(comboPostDto).getId(),
                ResponseLevel.ONE_LEVEL_DEPTH
        );

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.ACTION_SUCCESS)
                        .data(comboGetDto)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable Long id, @RequestBody ComboPostDto comboPostDto) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.ACTION_SUCCESS)
                        .data(comboService.getComboDtoById(
                                comboService.updateCombo(id, comboPostDto).getId(),
                                ResponseLevel.ONE_LEVEL_DEPTH)
                        )
                        .build()
        );
    }


}