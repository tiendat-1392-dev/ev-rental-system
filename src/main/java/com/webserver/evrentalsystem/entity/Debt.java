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
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    private User debtor;

    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
    private User creditor;

    @Column(name = "amount_owed", nullable = false)
    private Integer amountOwed;

    @Column(name = "owed_date", nullable = false)
    private Long owedDate;

    @ManyToOne
    @JoinColumn(name = "confirmed_payment_by")
    private User confirmedPaymentBy;

    @Column(name = "paid_date")
    private Long paidDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_paid", nullable = false, columnDefinition = "boolean default false")
    private Boolean isPaid;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @Column(name = "delete_at")
    private Long deleteAt;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
