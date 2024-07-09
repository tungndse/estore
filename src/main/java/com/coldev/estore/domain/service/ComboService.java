package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.combo.request.ComboFilterRequest;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.entity.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComboService {

    Combo createNewCombo(ComboPostDto payload);

    ComboGetDto getComboDtoById(Long id, ResponseLevel responseLevel);

    Combo getComboById(Long id);

    Combo getComboByIdWithNullCheck(Long id);

    Combo getComboByIdWithAvailabilityCheck(Long id);

    Combo updateCombo(Long id, ComboPostDto comboPostDto);

    List<Combo> getComboListByProductId(Long productId);

    List<ComboGetDto> getComboDtoList(ComboFilterRequest comboFilterRequest, ResponseLevel responseLevel);

    Page<Combo> getComboPage(ComboFilterRequest comboFilterRequest, Pageable pageable);

    Combo deleteCombo(Long id);
}
