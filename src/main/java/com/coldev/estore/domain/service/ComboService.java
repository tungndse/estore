package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.entity.Combo;

public interface ComboService {

    Combo createNewCombo(ComboPostDto payload);

    ComboGetDto getComboDtoById(Long id, ResponseLevel responseLevel);

    Combo updateCombo(Long id, ComboPostDto comboPostDto);
}
