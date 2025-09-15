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
@Table(name = "played_time")
public class PlayedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "computerName", nullable = false)
    private String computerName;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name="startTime", nullable = false)
    private Long startTime;

    @Column(name="playedTime", nullable = false)
    private Integer playedTimeInMinutes;

    @Column(name = "createdAt", nullable = false)
    private Long createdAt;

    @Column(name = "createdBy", nullable = false)
    private String createdBy;
}
