package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.UpdatePlayedTimeRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.PlayedTimeItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface ModeratorPlayedTimeManagementService {
    PagingResponse<PlayedTimeItem> getPlayedTime(Long timestamp, String searchTerm, HttpServletRequest httpRequest);
    void updatePlayedTime(UpdatePlayedTimeRequest requestBody, HttpServletRequest httpRequest);
}
