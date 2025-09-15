package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.UpdatePlayedTimeRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.PlayedTimeItem;
import com.webserver.evrentalsystem.service.ModeratorPlayedTimeManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/moderator/played-time")
public class ModeratorPlayedTimeManagementController {

    @Autowired
    private ModeratorPlayedTimeManagementService moderatorPlayedTimeManagementService;

    @GetMapping(value = "/get-played-time")
    public ResponseEntity<PagingResponse<PlayedTimeItem>> getPlayedTime(
            @RequestParam Long date,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest httpRequest
    ) {
        PagingResponse<PlayedTimeItem> data = moderatorPlayedTimeManagementService.getPlayedTime(date, searchTerm, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/update-played-time")
    public ResponseEntity<String> updatePlayedTime(
            @RequestBody UpdatePlayedTimeRequest requestBody,
            HttpServletRequest httpRequest
    ) {
        moderatorPlayedTimeManagementService.updatePlayedTime(requestBody, httpRequest);
        return ResponseEntity.ok("Success");
    }
}
