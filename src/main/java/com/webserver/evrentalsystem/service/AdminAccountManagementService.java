package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminAccountManagementService {
    void createNewUserAccount(CreateNewUserAccountRequest request, HttpServletRequest httpRequest);
    PagingResponse<UserDataItem> getUsers(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpRequest);
    void changeUserPassword(ForceChangeUserPasswordRequest request, HttpServletRequest httpRequest);
    void blockUser(BlockUserRequest request, HttpServletRequest httpRequest);
    void unblockUser(Long userId, HttpServletRequest httpRequest);
    List<ModeratorPermissionDto> fetchModeratorPermission(Long userId, HttpServletRequest httpRequest);
    void changeModeratorPermission(ChangeModeratorPermissionRequest request, HttpServletRequest httpRequest);
}
