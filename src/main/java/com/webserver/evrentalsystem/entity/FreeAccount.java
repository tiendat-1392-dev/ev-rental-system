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
@Table(name = "free_account")
public class FreeAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accountName", nullable = false)
    private String accountName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "timeToUseInMinutes", nullable = false)
    private Integer timeToUseInMinutes;

    @Column(name="startTimeAllowedToLogin", nullable = false)
    private Long startTimeAllowedToLogin;

    @Column(name="endTimeAllowedToLogin", nullable = false)
    private Long endTimeAllowedToLogin;

    @Column(name = "createdAt", nullable = false)
    private Long createdAt;

    @Column(name = "createdBy", nullable = false)
    private String createdBy;
}
