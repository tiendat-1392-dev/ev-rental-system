package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.RejectTopupRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.TopupHistoryItem;
import com.webserver.evrentalsystem.model.dto.table.TopupRequestPagingResponse;
import com.webserver.evrentalsystem.service.ModeratorTopupRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/moderator/topup-request")
public class ModeratorTopupRequestController {

    @Autowired
    private ModeratorTopupRequestService moderatorTopupRequestService;

    @GetMapping(value = "/get-topup-requests")
    public ResponseEntity<TopupRequestPagingResponse> getTopupRequests(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "30") Integer pageSize,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest httpRequest
    ) {
        TopupRequestPagingResponse data = moderatorTopupRequestService.getTopupRequests(pageNo, pageSize, searchTerm, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PatchMapping(value = "/approve-topup-requests")
    public ResponseEntity<String> approveTopupRequests(
            @RequestBody List<Long> topupRequestIds,
            HttpServletRequest httpRequest
    ) {
        moderatorTopupRequestService.approveTopupRequests(topupRequestIds, httpRequest);
        return ResponseEntity.ok("OK");
    }

    @PostMapping(value = "/reject-topup-requests")
    public ResponseEntity<String> rejectTopupRequest(
            @RequestBody RejectTopupRequest requestBody,
            HttpServletRequest httpRequest
    ) {
        moderatorTopupRequestService.rejectTopupRequest(requestBody, httpRequest);
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/get-topup-request-history")
    public ResponseEntity<PagingResponse<TopupHistoryItem>> getTopupRequestHistory(
            @RequestParam Long startDate,
            @RequestParam Long endDate,
            @RequestParam String status,
            @RequestParam(defaultValue = "") String searchBy,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "30") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        PagingResponse<TopupHistoryItem> data = moderatorTopupRequestService.getTopupRequestHistory(startDate, endDate, status, searchBy, searchTerm, pageNo, pageSize, httpRequest);
        return ResponseEntity.ok(data);
    }
}
