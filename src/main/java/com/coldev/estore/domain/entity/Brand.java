package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(fetch = FetchType.LAZY)
    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Basic(fetch = FetchType.LAZY)
    @Nationalized
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 10)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<Product> products;

}