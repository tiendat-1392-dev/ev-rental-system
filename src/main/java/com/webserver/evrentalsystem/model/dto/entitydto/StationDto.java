package com.webserver.evrentalsystem.model.dto.entitydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}