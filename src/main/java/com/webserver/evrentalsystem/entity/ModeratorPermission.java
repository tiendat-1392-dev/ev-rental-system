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
@Table(name = "moderator_permission")
public class ModeratorPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "permission_id", nullable = false)
    private String permission;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
