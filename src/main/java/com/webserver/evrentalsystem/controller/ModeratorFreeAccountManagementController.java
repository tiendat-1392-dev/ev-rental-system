package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.UpdateFreeAccountRequest;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForModerator;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.service.ModeratorFreeAccountManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/moderator/free-account")
public class ModeratorFreeAccountManagementController {

    @Autowired
    private ModeratorFreeAccountManagementService moderatorFreeAccountManagementService;

    @GetMapping(value = "/get-free-account")
    public ResponseEntity<PagingResponse<FreeAccountItemForModerator>> getFreeAccount(
            @RequestParam Long date,
            HttpServletRequest httpRequest
    ) {
        PagingResponse<FreeAccountItemForModerator> data = moderatorFreeAccountManagementService.getFreeAccount(date, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/update-free-account")
    public ResponseEntity<String> updateFreeAccount(
            @RequestBody UpdateFreeAccountRequest requestBody,
            HttpServletRequest httpRequest
    ) {
        moderatorFreeAccountManagementService.updateFreeAccount(requestBody, httpRequest);
        return ResponseEntity.ok("Success");
    }
}
