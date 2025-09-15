package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.DebtDataItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.TableDataResponse;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import com.webserver.evrentalsystem.service.ModeratorAccountManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/moderator/account")
public class ModeratorAccountManagementController {

    @Autowired
    private ModeratorAccountManagementService moderatorAccountManagementService;

    @PostMapping(value = "/create-new-user")
    public void createNewUser(@RequestBody CreateNewUserAccountRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.createNewUserAccount(requestBody, httpRequest);
    }

    @GetMapping(value = "/get-users")
    public ResponseEntity<PagingResponse<UserDataItem>> getUsers(
        @RequestParam(defaultValue = "1") Integer pageNo,
        @RequestParam(defaultValue = "30") Integer pageSize,
        @RequestParam(defaultValue = "") String searchTerm,
        HttpServletRequest httpRequest
    ) {
        PagingResponse<UserDataItem> data = moderatorAccountManagementService.getUsers(pageNo, pageSize, searchTerm, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PatchMapping(value = "/change-user-password")
    public void changeUserPassword(@RequestBody ForceChangeUserPasswordRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.changeUserPassword(requestBody, httpRequest);
    }

    @PostMapping(value = "/block-user")
    public void blockUser(@RequestBody BlockUserRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.blockUser(requestBody, httpRequest);
    }

    @PatchMapping(value = "/unblock-user/{userId}")
    public void unblockUser(@PathVariable("userId") Long userId, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.unblockUser(userId, httpRequest);
    }

    @GetMapping(value = "/get-user-info/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable("userId") Long userId, HttpServletRequest httpRequest) {
        UserDto data = moderatorAccountManagementService.getUserInfo(userId, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PatchMapping(value = "/update-user-info/{userId}")
    public void updateUserInfo(@PathVariable("userId") Long userId, @RequestBody UpdateUserInfoRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.updateUserInfo(userId, requestBody, httpRequest);
    }

    @GetMapping(value = "/get-debts/{userId}")
    public ResponseEntity<TableDataResponse<DebtDataItem>> getAllDebts(@PathVariable("userId") Long userId, HttpServletRequest httpRequest) {
        TableDataResponse<DebtDataItem> data = moderatorAccountManagementService.getAllDebtsByUser(userId, httpRequest);
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/create-new-debt")
    public void createNewDebt(@RequestBody CreateNewDebtRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.createNewDebt(requestBody, httpRequest);
    }

    @PostMapping(value = "/confirm-debt-payment")
    public void confirmDebtPayment(@RequestBody PayDebtRequest requestBody, HttpServletRequest httpRequest) {
        moderatorAccountManagementService.confirmDebtPayment(requestBody, httpRequest);
    }
}
