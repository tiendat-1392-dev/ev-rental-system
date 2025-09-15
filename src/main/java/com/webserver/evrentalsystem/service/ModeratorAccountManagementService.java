package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.DebtDataItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.TableDataResponse;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface ModeratorAccountManagementService {
    void createNewUserAccount(CreateNewUserAccountRequest request, HttpServletRequest httpRequest);
    PagingResponse<UserDataItem> getUsers(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpRequest);
    void changeUserPassword(ForceChangeUserPasswordRequest request, HttpServletRequest httpRequest);
    void blockUser(BlockUserRequest request, HttpServletRequest httpRequest);
    void unblockUser(Long userId, HttpServletRequest httpRequest);
    UserDto getUserInfo(Long userId, HttpServletRequest httpRequest);
    void updateUserInfo(Long userId, UpdateUserInfoRequest request, HttpServletRequest httpRequest);
    TableDataResponse<DebtDataItem> getAllDebtsByUser(Long userId, HttpServletRequest httpRequest);
    void createNewDebt(CreateNewDebtRequest request, HttpServletRequest httpRequest);
    void confirmDebtPayment(PayDebtRequest request, HttpServletRequest httpRequest);
}
