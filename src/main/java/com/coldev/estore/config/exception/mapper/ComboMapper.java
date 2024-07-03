package com.coldev.estore.config.exception.mapper;


import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.entity.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        //, uses = {AddressMapper.class, PasswordValidator.class, FormMapper.class}
)
public interface ComboMapper {

    ComboMapper INSTANCE = Mappers.getMapper(ComboMapper.class);

    default ComboGetDto toComboGetDto(Combo combo) {
        if (combo == null) return null;

        ComboGetDto.ComboGetDtoBuilder comboGetDtoBuilder = ComboGetDto.builder()
                .id(combo.getId())
                .name(combo.getName())
                .description(combo.getDescription())
                .discountPercentage(combo.getDiscountPercentage())
                .discountValue(combo.getDiscountValue())
                .status(combo.getStatus());

        if (combo.getMedia() != null) {
            comboGetDtoBuilder.imgUrl(combo.getMedia().getUrl());
        }

        return comboGetDtoBuilder.build();
    }

    default ComboGetDto.ComboGetDtoBuilder toComboGetDtoBuilder(Combo combo) {
        if (combo == null) return null;

        ComboGetDto.ComboGetDtoBuilder comboGetDtoBuilder = ComboGetDto.builder()
                .id(combo.getId())
                .name(combo.getName())
                .description(combo.getDescription())
                .discountPercentage(combo.getDiscountPercentage())
                .discountValue(combo.getDiscountValue())
                .status(combo.getStatus());

        if (combo.getMedia() != null) {
            comboGetDtoBuilder.imgUrl(combo.getMedia().getUrl());
        }

        return comboGetDtoBuilder;
    }

    default Combo.ComboBuilder toNewComboBuilder(ComboPostDto comboPostDto) {
        if (comboPostDto == null) return null;

        return Combo.builder()
                .name(comboPostDto.getName())
                .description(comboPostDto.getDescription())
                .discountPercentage(comboPostDto.getDiscountPercentage())
                .discountValue(comboPostDto.getDiscountValue())
                .status(comboPostDto.getStatus());

    }
}
