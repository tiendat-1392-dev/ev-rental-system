package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViolationRepository extends JpaRepository<Violation, Long> {
    List<Violation> findByRentalId(Long rentalId);
}
