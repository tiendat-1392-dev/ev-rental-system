package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.Permission;
import com.webserver.evrentalsystem.entity.FreeAccount;
import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.dto.FreeAccountRequestData;
import com.webserver.evrentalsystem.model.dto.UpdateFreeAccountRequest;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForModerator;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.mapping.FreeAccountMapping;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.FreeAccountRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.ModeratorFreeAccountManagementService;
import com.webserver.evrentalsystem.service.validation.ManagerValidation;
import com.webserver.evrentalsystem.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ModeratorFreeAccountManagementServiceImpl implements ModeratorFreeAccountManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @Autowired
    private FreeAccountRepository freeAccountRepository;

    @Override
    public PagingResponse<FreeAccountItemForModerator> getFreeAccount(Long timestamp, HttpServletRequest httpRequest) {

        if (timestamp == null || timestamp <= 0) {
            throw new InvalidateParamsException("Invalid timestamp");
        }

        Long startDate = DateUtils.getStartOfDay(timestamp);
        Long endDate = DateUtils.getEndOfDay(timestamp);

        ManagerValidation.validateManager(userRepository, httpRequest);

        List<FreeAccount> freeAccountList = freeAccountRepository.findByDateBetween(startDate, endDate);

        PagingResponse<FreeAccountItemForModerator> data = new PagingResponse<>();
        data.setCurrentPage(1);
        data.setTotalPages(1);
        data.setTotalElements(freeAccountList.size());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Tài khoản", false));
        headerItemList.add(new HeaderItem("Mật khẩu", false));
        headerItemList.add(new HeaderItem("Số phút chơi", false));
        headerItemList.add(new HeaderItem("Bắt đầu đăng nhập", false));
        headerItemList.add(new HeaderItem("Kết thúc đăng nhập", false));

        data.setHeader(headerItemList);

        List<FreeAccountItemForModerator> rows = new ArrayList<>();
        for (FreeAccount freeAccount : freeAccountList) {
            rows.add(FreeAccountMapping.toFreeAccountItem(freeAccount));
        }

        data.setRows(rows);
        data.setSearchTerm("");

        return data;
    }

    @Override
    public void updateFreeAccount(UpdateFreeAccountRequest requestBody, HttpServletRequest httpRequest) {

        if (!requestBody.isValid()) {
            throw new InvalidateParamsException("Invalid request body");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.UPDATE_FREE_ACCOUNT.getKey())) {
            throw new PermissionDeniedException("Bạn không có quyền thực hiện chức năng này");
        }

        Long startOfDay = DateUtils.getStartOfDay(requestBody.getDate());
        Long endOfDay = DateUtils.getEndOfDay(requestBody.getDate());

        freeAccountRepository.deleteByDateBetween(startOfDay, endOfDay);

        for (FreeAccountRequestData freeAccountRequestData : requestBody.getData()) {

            if (freeAccountRequestData.getStartTimeAllowedToLogin() < startOfDay || freeAccountRequestData.getEndTimeAllowedToLogin() > endOfDay) {
                throw new InvalidateParamsException("Đã xảy ra lỗi khi cập nhật thời gian chơi.");
            }

            FreeAccount freeAccount = FreeAccountMapping.toFreeAccount(freeAccountRequestData);
            freeAccount.setCreatedAt(System.currentTimeMillis());
            freeAccount.setCreatedBy(manager.getUserName());
            freeAccountRepository.save(freeAccount);
        }
    }
}
