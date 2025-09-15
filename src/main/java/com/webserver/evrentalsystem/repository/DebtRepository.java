package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Debt;
import com.webserver.evrentalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    @Query("SELECT d FROM Debt d WHERE d.debtor = :debtor AND d.isPaid = false  AND d.isDeleted = false")
    List<Debt> findAllUnpaidDebtsByDebtor(User debtor);
}
