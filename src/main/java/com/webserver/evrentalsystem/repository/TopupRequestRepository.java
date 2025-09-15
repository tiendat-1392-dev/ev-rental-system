package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.TopupRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopupRequestRepository extends JpaRepository<TopupRequest, Long> {

    @Query("SELECT r FROM TopupRequest r WHERE r.isApproved = false AND r.isRejected = false AND r.isDeleted = false AND r.user.userName LIKE %:searchTerm%")
    Page<TopupRequest> findAllWaitingRequestIn(String searchTerm, Pageable pageable);

    // sort by created date
    @Query("SELECT r FROM TopupRequest r WHERE r.isApproved = false AND r.isRejected = false AND r.isDeleted = false AND r.user.userName = :userName ORDER BY r.createdAt DESC")
    List<TopupRequest> findAllPendingTopupRequestByUserName(String userName);
}
