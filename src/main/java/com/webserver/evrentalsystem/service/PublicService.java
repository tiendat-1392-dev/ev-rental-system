package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForGuest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PublicService {
    List<FreeAccountItemForGuest> getFreeAccount(HttpServletRequest httpRequest);
}
