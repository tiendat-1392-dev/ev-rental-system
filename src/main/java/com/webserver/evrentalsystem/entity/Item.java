package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.ItemTypeJpaConverter;
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
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = ItemTypeJpaConverter.class)
    @Column(name = "type", nullable = false)
    private ItemType type;

    @ManyToOne
    @JoinColumn(name = "voucherId")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "luckyWheelId")
    private LuckyWheel luckyWheel;
}
