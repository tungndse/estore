package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(fetch = FetchType.LAZY)
    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Nationalized
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Size(max = 50)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Nationalized
    @Lob
    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @Builder.Default
    private Date createdAt = new Date();

    @Size(max = 10)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

}