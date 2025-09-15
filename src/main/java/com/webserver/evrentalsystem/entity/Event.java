package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.EventTypeJpaConverter;
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
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Convert(converter = EventTypeJpaConverter.class)
    @Column(name = "eventType", nullable = false)
    private EventType eventType;

    @Column(name = "description")
    private String description;

    @Column(name = "startTime", nullable = false)
    private Long startTime;

    @Column(name = "endTime", nullable = false)
    private Long endTime;

    @Column(name = "status", nullable = false)
    private Boolean status;
}
