package com.coldev.estore.config.exception.mapper;


import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.entity.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        //, uses = {AddressMapper.class, PasswordValidator.class, FormMapper.class}
)
public interface ComboMapper {

    default ComboGetDto toComboGetDto(Combo combo) {
        if (combo == null) return null;

        return ComboGetDto.builder()
                .id(combo.getId())
                .name(combo.getName())
                .description(combo.getDescription())
                .discountPercentage(combo.getDiscountPercentage())
                .discountValue(combo.getDiscountValue())
                .imgUrl(combo.getImgUrl())
                .status(combo.getStatus())
                .build();
    }

    default ComboGetDto.ComboGetDtoBuilder toComboGetDtoBuilder(Combo combo) {
        if (combo == null) return null;

        return ComboGetDto.builder()
                .id(combo.getId())
                .name(combo.getName())
                .description(combo.getDescription())
                .discountPercentage(combo.getDiscountPercentage())
                .discountValue(combo.getDiscountValue())
                .imgUrl(combo.getImgUrl())
                .status(combo.getStatus());
    }

    default Combo.ComboBuilder toNewComboBuilder(ComboPostDto comboPostDto) {
        if (comboPostDto == null) return null;

        return Combo.builder()
                .name(comboPostDto.getName())
                .description(comboPostDto.getDescription())
                .discountPercentage(comboPostDto.getDiscountPercentage())
                .discountValue(comboPostDto.getDiscountValue())
                .imgUrl(comboPostDto.getImgUrl())
                .status(comboPostDto.getStatus());

    }
}
