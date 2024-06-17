package com.coldev.estore.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "media")
public class Media {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "media_url", length = -1)
    private String url;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "media_key", nullable = false, length = -1)
    private String key;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "media_type", nullable = false, length = -1)
    private String mediaType;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<Product> products;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<Account> accounts;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<Combo> combos;

    @ManyToMany(mappedBy = "subMediaList", fetch = FetchType.LAZY)
    @JsonIgnore
    Collection<Product> productsWithMediaAsSub;

}
