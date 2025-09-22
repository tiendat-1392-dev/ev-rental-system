package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StationMapper.class})
public interface VehicleMapper {
    VehicleDto toVehicleDto(Vehicle vehicle);
}
