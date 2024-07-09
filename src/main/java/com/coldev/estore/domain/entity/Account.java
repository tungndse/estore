package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private Long id;

    @Size(max = 100)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "username", nullable = false, length = 100, unique = true)
    private String username;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "name", nullable = false)
    private String name;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description", nullable = false)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "password", nullable = false)
    private String password;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "address")
    private String address;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "address_longitude")
    private Double addressLongitude;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "address_latitude")
    private Double addressLatitude;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "email", nullable = false)
    private String email;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "phone")
    private String phone;

    @Size(max = 10)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Size(max = 10)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Media media;

    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @Builder.Default
    private Date createdAt = new Date();


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<CustomerOrder> customerOrders;


}