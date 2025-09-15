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
@Table(name = "topup_request")
public class TopupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "is_approved", columnDefinition = "boolean default false")
    private Boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private Long approvedAt;

    @Column(name = "is_rejected", columnDefinition = "boolean default false")
    private Boolean isRejected;

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    @Column(name = "rejected_at")
    private Long rejectedAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private Long deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;
}
