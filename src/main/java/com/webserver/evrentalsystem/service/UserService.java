package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.TopupRequestDtoForUser;
import com.webserver.evrentalsystem.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto getUserInfo(HttpServletRequest request);
    List<TopupRequestDtoForUser> getTopupRequests(HttpServletRequest request);
    void createTopupRequest(HttpServletRequest request, Integer amount);
}
