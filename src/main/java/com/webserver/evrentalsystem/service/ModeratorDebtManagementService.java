package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.UpdateDebtRequest;
import com.webserver.evrentalsystem.model.dto.table.DebtInfoItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface ModeratorDebtManagementService {

    void deleteDebtById(Long debtId, HttpServletRequest httpRequest);

    void restoreDebtById(Long debtId, HttpServletRequest httpRequest);

    void updateDebt(UpdateDebtRequest updateDebtRequest, HttpServletRequest httpRequest);

    PagingResponse<DebtInfoItem> getDebts(
            String byDate,
            Long startDate,
            Long endDate,
            Boolean isPaid,
            String searchBy,
            String searchTerm,
            Integer pageNo,
            Integer pageSize,
            HttpServletRequest httpRequest
    );
}
