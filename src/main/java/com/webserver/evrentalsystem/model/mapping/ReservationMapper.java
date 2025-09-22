package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, StationMapper.class})
public interface ReservationMapper {
    ReservationDto toReservationDto(Reservation reservation);
}
