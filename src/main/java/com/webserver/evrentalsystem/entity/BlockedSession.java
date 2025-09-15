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
@Table(name = "blocked_session")
public class BlockedSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser;

    @Column(name = "reason")
    private String reason;

    @Column(name = "block_time")
    private Long blockTime;

    @Column(name = "unblock_time")
    private Long unblockTime;

    @Column(name = "is_unlocked")
    private Boolean isUnblocked;
}
