package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.UpdateDebtRequest;
import com.webserver.evrentalsystem.model.dto.table.DebtInfoItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.service.ModeratorDebtManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/moderator/debt-management")
public class ModeratorDebtManagementController {

    @Autowired
    private ModeratorDebtManagementService moderatorDebtManagementService;

    @PatchMapping(value = "/delete-debt-by-id/{debtId}")
    public void deleteDebtById(
            @PathVariable(value = "debtId") Long debtId,
            HttpServletRequest httpRequest
    ) {
        moderatorDebtManagementService.deleteDebtById(debtId, httpRequest);
    }

    @PatchMapping(value = "/restore-debt-by-id/{debtId}")
    public void restoreDebtById(
            @PathVariable(value = "debtId") Long debtId,
            HttpServletRequest httpRequest
    ) {
       moderatorDebtManagementService.restoreDebtById(debtId, httpRequest);
    }

    @PostMapping(value = "/update-debt")
    public void updateDebt(
            @RequestBody UpdateDebtRequest updateDebtRequest,
            HttpServletRequest httpRequest
    ) {
        moderatorDebtManagementService.updateDebt(updateDebtRequest, httpRequest);
    }

    @GetMapping(value = "/get-debts")
    public ResponseEntity<PagingResponse<DebtInfoItem>> getDebts(
            @RequestParam String byDate,
            @RequestParam Long startDate,
            @RequestParam Long endDate,
            @RequestParam Boolean isPaid,
            @RequestParam(defaultValue = "") String searchBy,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "30") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        PagingResponse<DebtInfoItem> data = moderatorDebtManagementService.getDebts(byDate, startDate, endDate, isPaid, searchBy, searchTerm, pageNo, pageSize, httpRequest);
        return ResponseEntity.ok(data);
    }
}
