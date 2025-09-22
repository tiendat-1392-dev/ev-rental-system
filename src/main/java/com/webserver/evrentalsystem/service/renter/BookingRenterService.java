package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.CreateReservationRequest;

import java.util.List;

public interface BookingRenterService {
    List<StationDto> getStations();
    List<VehicleDto> getVehicles(String type, Long stationId, Double priceMin, Double priceMax);
    ReservationDto createReservation(CreateReservationRequest request);
    List<ReservationDto> getMyReservations();
    ReservationDto getReservationById(Long id);
    void cancelReservation(Long id, String cancelReason);
}
