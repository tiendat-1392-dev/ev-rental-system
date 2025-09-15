package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import com.webserver.evrentalsystem.service.AdminAccountManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/admin/account")
public class AdminAccountManagementController {

    @Autowired
    private AdminAccountManagementService adminAccountManagementService;

    @PostMapping(value = "/create-new-user")
    public void createNewUser(@RequestBody CreateNewUserAccountRequest requestBody, HttpServletRequest httpRequest) {
        adminAccountManagementService.createNewUserAccount(requestBody, httpRequest);
    }

    @GetMapping(value = "/get-users")
    public ResponseEntity<PagingResponse<UserDataItem>> getUsers(
        @RequestParam(defaultValue = "1") Integer pageNo,
        @RequestParam(defaultValue = "30") Integer pageSize,
        @RequestParam(defaultValue = "") String searchTerm,
        HttpServletRequest httpRequest
    ) {
        PagingResponse<UserDataItem> data = adminAccountManagementService.getUsers(pageNo, pageSize, searchTerm, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PatchMapping(value = "/change-user-password")
    public void changeUserPassword(@RequestBody ForceChangeUserPasswordRequest requestBody, HttpServletRequest httpRequest) {
        adminAccountManagementService.changeUserPassword(requestBody, httpRequest);
    }

    @PostMapping(value = "/block-user")
    public void blockUser(@RequestBody BlockUserRequest requestBody, HttpServletRequest httpRequest) {
        adminAccountManagementService.blockUser(requestBody, httpRequest);
    }

    @PatchMapping(value = "/unblock-user/{userId}")
    public void unblockUser(@PathVariable("userId") Long userId, HttpServletRequest httpRequest) {
        adminAccountManagementService.unblockUser(userId, httpRequest);
    }

    @GetMapping(value = "/fetch-moderator-permission")
    public ResponseEntity<List<ModeratorPermissionDto>> fetchModeratorPermission(
        @RequestParam("userId") Long userId,
        HttpServletRequest httpRequest
    ) {
        List<ModeratorPermissionDto> data = adminAccountManagementService.fetchModeratorPermission(userId, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PatchMapping(value = "/change-moderator-permission")
    public void changeModeratorPermission(@RequestBody ChangeModeratorPermissionRequest requestBody, HttpServletRequest httpRequest) {
        adminAccountManagementService.changeModeratorPermission(requestBody, httpRequest);
    }
}
