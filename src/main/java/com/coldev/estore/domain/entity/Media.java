package com.coldev.estore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
