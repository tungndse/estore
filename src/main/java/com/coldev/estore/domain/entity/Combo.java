package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

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

    @OneToMany(mappedBy = "combo", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<CustomerOrder> customerOrders;

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