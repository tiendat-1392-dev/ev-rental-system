package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.request.RentalCheckInRequest;
import com.webserver.evrentalsystem.model.dto.request.ReservationFilterRequest;
import com.webserver.evrentalsystem.service.staff.RentalStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/staff/rentals")
@Tag(name = "3.1. Staff Rental", description = "API quản lý thuê xe của nhân viên (Staff)")
@SecurityRequirement(name = "bearerAuth")
public class RentalStaffController {

    @Autowired
    private RentalStaffService rentalStaffService;

    @Operation(summary = "Lấy danh sách Reservation theo bộ lọc")
    @GetMapping("/reservations")
    public List<ReservationDto> getReservations(
            @Parameter(description = "ID của renter (người thuê)")
            @RequestParam(required = false) Long renterId,

            @Parameter(description = "ID của vehicle (xe)")
            @RequestParam(required = false) Long vehicleId,

            @Parameter(
                    description = "Trạng thái của Reservation",
                    example = "pending",
                    schema = @Schema(allowableValues = {"pending", "confirmed", "cancelled", "expired"})
            )
            @RequestParam(required = false) String status,

            @Parameter(description = "Thời gian bắt đầu (lọc từ)", example = "2025-09-23T08:00:00")
            @RequestParam(required = false) LocalDateTime startFrom,

            @Parameter(description = "Thời gian bắt đầu (lọc đến)")
            @RequestParam(required = false) LocalDateTime startTo
    ) {
        ReservationFilterRequest filter = new ReservationFilterRequest(
                renterId, vehicleId, status, startFrom, startTo
        );
        return rentalStaffService.getReservations(filter);
    }

    @Operation(summary = "Check-in nhận xe (booking hoặc walk-in)")
    @PostMapping("/check-in")
    public RentalDto checkIn(@Valid @RequestBody RentalCheckInRequest request) {
        return rentalStaffService.checkIn(request);
    }
}
