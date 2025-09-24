package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.request.RentalCheckInRequest;
import com.webserver.evrentalsystem.model.dto.request.ReservationFilterRequest;
import com.webserver.evrentalsystem.model.mapping.RentalMapper;
import com.webserver.evrentalsystem.model.mapping.ReservationMapper;
import com.webserver.evrentalsystem.repository.*;
import com.webserver.evrentalsystem.service.staff.RentalStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.specification.ReservationSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class RentalStaffServiceImpl implements RentalStaffService {

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    public List<ReservationDto> getReservations(ReservationFilterRequest filter) {
        List<Reservation> reservations = reservationRepository.findAll(
                Specification.where(ReservationSpecification.hasRenter(filter.getRenterId()))
                        .and(ReservationSpecification.hasVehicle(filter.getVehicleId()))
                        .and(ReservationSpecification.hasStatus(ReservationStatus.fromValue(filter.getStatus())))
                        .and(ReservationSpecification.startFrom(filter.getStartFrom()))
                        .and(ReservationSpecification.startTo(filter.getStartTo()))
        );

        return reservations.stream()
                .map(reservationMapper::toReservationDto)
                .toList();
    }

    @Override
    public RentalDto checkIn(RentalCheckInRequest request) {
        User staff = userValidation.validateStaff();
        Long renterId = request.getRentalId();
        Long reservationId = request.getReservationId();
        Long vehicleId = request.getVehicleId();
        Long stationId = request.getStationId();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        BigDecimal depositAmount = request.getDepositAmount();

        User renter = userRepository.findById(renterId).orElse(null);
        if (renter == null) {
            throw new NotFoundException("Người thuê (renter) không tồn tại");
        }

        if (reservationId == null && vehicleId == null) {
            throw new InvalidateParamsException("Phải cung cấp reservationId hoặc vehicleId");
        }

        if (reservationId != null && vehicleId != null) {
            throw new InvalidateParamsException("Chỉ được cung cấp reservationId hoặc vehicleId");
        }

        if (depositAmount != null && depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidateParamsException("Tiền cọc phải lớn hơn hoặc bằng 0");
        }

        Vehicle vehicle;
        LocalDateTime finalStartTime;
        LocalDateTime finalEndTime;
        RentalType rentalType;
        if (reservationId != null) {
            // Đã book trước
            Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                throw new ConflictException("Reservation không tồn tại");
            }
            // Kiểm tra reservation thuộc về renter
            if (!reservation.getRenter().getId().equals(renter.getId())) {
                throw new ConflictException("Reservation không thuộc về người dùng");
            }
            // Kiểm tra reservation ở trạng thái PENDING
            if (reservation.getStatus() != ReservationStatus.PENDING) {
                throw new ConflictException("Reservation không ở trạng thái PENDING nên có thể đã được check-in hoặc hủy hoặc hết hạn");
            }
            // Cập nhật trạng thái reservation thành CONFIRMED
            reservation.setStatus(ReservationStatus.CONFIRMED);
            vehicle = reservation.getVehicle();
            finalStartTime = reservation.getReservedStartTime();
            finalEndTime = reservation.getReservedEndTime();
            rentalType = RentalType.BOOKING;
        } else {
            if (startTime == null || endTime == null) {
                throw new InvalidateParamsException("Phải cung cấp startTime, endTime và depositAmount khi thuê walk-in");
            }
            // Đến thuê trực tiếp (walk-in)
            vehicle = vehicleRepository.findById(vehicleId).orElse(null);
            if (vehicle == null) {
                throw new ConflictException("Xe không tồn tại");
            }
            finalStartTime = startTime;
            finalEndTime = endTime;
            rentalType = RentalType.WALK_IN;
        }

        // Kiểm tra xe
        if (!Objects.equals(vehicle.getStation().getId(), stationId)) {
            throw new ConflictException("Phải đến đúng trạm đã chọn khi đặt trước để nhận xe");
        }
        // Kiểm tra xe ở trạng thái AVAILABLE hoặc RESERVED
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE && vehicle.getStatus() != VehicleStatus.RESERVED) {
            throw new ConflictException("Xe không ở trạng thái AVAILABLE hoặc RESERVED");
        }

        // Tính tiền thuê dự tính dựa trên thời gian thuê
        if (finalEndTime.isBefore(finalStartTime) || finalEndTime.isEqual(finalStartTime)) {
            throw new InvalidateParamsException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }
        long hours = java.time.Duration.between(finalStartTime, finalEndTime).toHours();
        if (hours == 0) {
            hours = 1; // Thu tối thiểu 1 giờ
        } else if (java.time.Duration.between(finalStartTime, finalEndTime).toMinutes() % 60 != 0) {
            hours += 1; // Làm tròn lên nếu có phút lẻ
        }
        BigDecimal rentalCost = vehicle.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        // Kiểm tra tiền cọc
        BigDecimal minDeposit = rentalCost.multiply(BigDecimal.valueOf(0.3)); // Tiền cọc tối thiểu 30% tổng tiền thuê
        if (depositAmount != null && depositAmount.compareTo(minDeposit) < 0) {
            throw new ConflictException("Tiền cọc phải lớn hơn hoặc bằng 30% tổng tiền thuê là " +minDeposit);
        }

        Rental rental = new Rental();
        rental.setRenter(renter);
        rental.setVehicle(vehicle);
        rental.setStationPickup(vehicle.getStation());
        rental.setStaffPickup(staff);
        rental.setStartTime(finalStartTime);
        rental.setEndTime(finalEndTime);
        rental.setTotalCost(rentalCost);
        rental.setRentalType(rentalType);
        rental.setDepositAmount(depositAmount != null ? depositAmount : BigDecimal.ZERO);
        rental.setDepositStatus(DepositStatus.PENDING);
        rental.setStatus(RentalStatus.BOOKED);
        rental.setCreatedAt(LocalDateTime.now());

        return rentalMapper.toRentalDto(rentalRepository.save(rental));
    }
}
