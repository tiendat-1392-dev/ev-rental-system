package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.PlayedTime;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayedTimeRepository extends JpaRepository<PlayedTime, Long> {
    @Query("SELECT p FROM PlayedTime p WHERE p.startTime BETWEEN :startDate AND :endDate AND p.userName LIKE %:searchTerm%")
    List<PlayedTime> findByDateBetweenAndUserNameContaining(Long startDate, Long endDate, String searchTerm);

    @Transactional
    @Modifying
    @Query("DELETE FROM PlayedTime p WHERE p.startTime BETWEEN :startDate AND :endDate")
    void deleteByDateBetween(Long startDate, Long endDate);
}
