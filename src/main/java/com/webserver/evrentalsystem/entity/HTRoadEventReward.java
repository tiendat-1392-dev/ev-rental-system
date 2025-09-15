package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.HTRoadEventRewardTypeJpaConverter;
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
@Table(name = "ht_road_event_reward")
public class HTRoadEventReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    @Column(name = "requiredPlayedTimeInHour", nullable = false)
    private Integer requiredPlayedTimeInHour;

    @Convert(converter = HTRoadEventRewardTypeJpaConverter.class)
    @Column(name = "rewardType", nullable = false)
    private HTRoadEventRewardType rewardType;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Column(name = "rewardAmount", nullable = false)
    private Integer rewardAmount;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
