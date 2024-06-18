package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "combo")
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nationalized
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "name", nullable = false)
    private String name;

    @Nationalized
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description", nullable = false)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "discount_percentage")
    private Double discountPercentage;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "discount_value", precision = 19, scale = 4)
    private BigDecimal discountValue;

    @Size(max = 10)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private Media media;

    @ManyToMany(mappedBy = "combos", fetch = FetchType.LAZY)
    @JsonIgnore
    Collection<Product> products;

    /*public Combo.ComboBuilder merge(ComboPostDto comboPostDto) {
        return Combo.builder()
                .name(comboPostDto.getName())
                .description(comboPostDto.getDescription())
                .discountPercentage(comboPostDto.getDiscountPercentage())
                .discountValue(comboPostDto.getDiscountValue())
                .status(comboPostDto.getStatus())
                .imgUrl(comboPostDto.getImgUrl());
        
    }*/
}