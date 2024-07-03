package com.coldev.estore.domain.entity;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(fetch = FetchType.LAZY)
    @Nationalized
    @Column(name = "name")
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
    private OrderStatus status;

    @Size(max = 30)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "payment_method", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull
    @Column(name = "is_combo_order", nullable = false)
    private Boolean isComboOrder;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "discount_total", nullable = false, precision = 19, scale = 4)
    private BigDecimal discountTotal;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "net_amount", precision = 19, scale = 4)
    private BigDecimal netAmount;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @Builder.Default
    private Date createdAt = new Date();

    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Account customer;

    @OneToMany(mappedBy = "customerOrder", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<CustomerOrderItem> customerOrderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private Combo combo;



}