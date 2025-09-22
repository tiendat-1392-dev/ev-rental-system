package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Station;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StationMapper {
    StationDto toStationDto(Station station);
}
