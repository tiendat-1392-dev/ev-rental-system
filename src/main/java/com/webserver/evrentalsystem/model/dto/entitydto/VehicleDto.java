package com.webserver.evrentalsystem.model.dto.entitydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Long id;
    private String licensePlate;
    private String type;
    private String brand;
    private String model;
    private Integer capacity;
    private String status;
    private BigDecimal pricePerHour;
    private StationDto station;
}