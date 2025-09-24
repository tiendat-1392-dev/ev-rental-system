package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.request.RentalCheckInRequest;
import com.webserver.evrentalsystem.model.dto.request.ReservationFilterRequest;

import java.util.List;

public interface RentalStaffService {
    List<ReservationDto> getReservations(ReservationFilterRequest filter);
    RentalDto checkIn(RentalCheckInRequest request);
}
