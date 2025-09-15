package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.RejectTopupRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.TopupHistoryItem;
import com.webserver.evrentalsystem.model.dto.table.TopupRequestPagingResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ModeratorTopupRequestService {
    TopupRequestPagingResponse getTopupRequests(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpServletRequest);
    void approveTopupRequests(List<Long> topupRequestIds, HttpServletRequest httpRequest);
    void rejectTopupRequest(RejectTopupRequest request, HttpServletRequest httpRequest);
    PagingResponse<TopupHistoryItem> getTopupRequestHistory(Long startDate, Long endDate, String status, String searchBy, String searchTerm, Integer pageNo, Integer pageSize, HttpServletRequest httpRequest);
}
