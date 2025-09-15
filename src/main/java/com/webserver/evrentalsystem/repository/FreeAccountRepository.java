package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.FreeAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeAccountRepository extends JpaRepository<FreeAccount, Long> {
    @Query("SELECT p FROM FreeAccount p WHERE p.endTimeAllowedToLogin BETWEEN :startDate AND :endDate ORDER BY p.endTimeAllowedToLogin ASC")
    List<FreeAccount> findByDateBetween(Long startDate, Long endDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM FreeAccount p WHERE p.startTimeAllowedToLogin BETWEEN :startDate AND :endDate")
    void deleteByDateBetween(Long startDate, Long endDate);
}
