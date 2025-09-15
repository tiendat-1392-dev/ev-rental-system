package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

//    @Query("SELECT v FROM Voucher v WHERE v.code = :code AND v.isDeleted = false AND v.isActive = true AND v.expiredAt > CURRENT_TIMESTAMP")
//    Voucher findActiveByCode(String code);

    @Query("SELECT v FROM Voucher v WHERE v.isDeleted = false")
    Page<Voucher> findAllThatNotDeleted(Pageable pageable);

//    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Voucher v WHERE v.id = :id AND v.isDeleted = false AND v.isActive = true AND v.expiredAt > CURRENT_TIMESTAMP")
//    boolean isCanUse(String id);
}
