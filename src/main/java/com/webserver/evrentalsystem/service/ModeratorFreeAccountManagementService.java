package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.UpdateFreeAccountRequest;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForModerator;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface ModeratorFreeAccountManagementService {
    PagingResponse<FreeAccountItemForModerator> getFreeAccount(Long timestamp, HttpServletRequest httpRequest);
    void updateFreeAccount(UpdateFreeAccountRequest requestBody, HttpServletRequest httpRequest);
}
