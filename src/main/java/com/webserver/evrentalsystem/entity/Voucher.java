package com.webserver.evrentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "max_discount", nullable = false)
    private Integer maxDiscount;

    @Column(name = "discount_factor", nullable = false)
    private Double discountFactor;

    @Column(name = "min_topup", nullable = false)
    private Integer minTopup;

    @Column(name = "expired_at", nullable = false)
    private Long expiredAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private Long deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;
}
