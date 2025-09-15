package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.UserAlreadyExistsException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import com.webserver.evrentalsystem.model.mapping.UserMapping;
import com.webserver.evrentalsystem.repository.BlockedSessionRepository;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.AdminAccountManagementService;
import com.webserver.evrentalsystem.service.validation.ManagerValidation;
import com.webserver.evrentalsystem.utils.Constant;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminAccountManagementServiceImpl implements AdminAccountManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockedSessionRepository blockedSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @Override
    public void createNewUserAccount(CreateNewUserAccountRequest requestBody, HttpServletRequest httpRequest) {

        User manager = ManagerValidation.validateAdmin(userRepository, httpRequest);

        String userName = requestBody.getUserName();
        String password = requestBody.getPassword();

        User user = userRepository.findByUserName(userName);

        if (user != null) {
            throw new UserAlreadyExistsException("Người dùng đã tồn tại!");
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(Role.MODERATOR);
        newUser.setMembershipClass(MembershipClass.NEWBIE);
        newUser.setAmount(0);
        newUser.setCreatedAt(System.currentTimeMillis());
        newUser.setCreatedBy(manager.getUserName());

        userRepository.save(newUser);

        Logger.printf("Create new moderator account successfully with username: " + userName);
    }

    @Override
    public PagingResponse<UserDataItem> getUsers(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpRequest) {

        if (pageNo < 1 || pageSize < 1) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        Page<User> page = userRepository.findAllByRoleIn(Role.MODERATOR, searchTerm, PageRequest.of(Math.max((pageNo - 1), 0), pageSize));

        Logger.printf("Total elements: " + page.getTotalElements());

        PagingResponse<UserDataItem> data = new PagingResponse<>();

        data.setCurrentPage(pageNo);
        data.setTotalPages(page.getTotalPages());
        data.setTotalElements(page.getTotalElements());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", true));
        headerItemList.add(new HeaderItem("disabledSessionId", true));
        headerItemList.add(new HeaderItem("Tên đăng nhập", false));
        headerItemList.add(new HeaderItem("Tên công khai", true));
        headerItemList.add(new HeaderItem("Số dư", true));
        headerItemList.add(new HeaderItem("Tình trạng", false));
        headerItemList.add(new HeaderItem("Ngày tạo", false));

        data.setHeader(headerItemList);

        List<UserDataItem> userDataItemList = new ArrayList<>();
        for (User user : page.getContent()) {
            BlockedSession blockedSession = blockedSessionRepository.findAndCheckIfUserIsBlocked(user);

            long blockedSessionId = -1;
            if (blockedSession != null) {
                blockedSessionId = blockedSession.getId();
            }

            userDataItemList.add(UserMapping.toUserDataItem(user, blockedSessionId));
        }

        data.setRows(userDataItemList);
        data.setSearchTerm(searchTerm);

        return data;
    }

    @Override
    public void changeUserPassword(ForceChangeUserPasswordRequest request, HttpServletRequest httpRequest) {

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        long userId = request.getUserId();
        String newPassword = request.getNewPassword();

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidateParamsException("Mật khẩu không được để trống");
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        user.get().setRefreshToken(null); // logout all session
        userRepository.save(user.get());

        Logger.printf("Change user password successfully with username: " + user.get().getUserName());
    }

    @Override
    public void blockUser(BlockUserRequest request, HttpServletRequest httpRequest) {

        User manager = ManagerValidation.validateAdmin(userRepository, httpRequest);

        if (!request.isParamsValid()) {
            throw new InvalidateParamsException("Lý do không được để trống");
        }

        if (!request.isUnblockTimeValid()) {
            throw new InvalidateParamsException("Cần khoá ít nhất 1 ngày");
        }

        if (!request.isReasonValid()) {
            throw new InvalidateParamsException("Lý do tối đa " + Constant.MAXIMUM_REASONS_LENGTH + " ký tự");
        }

        long userId = request.getUserId();
        String reason = request.getReason();
        long unblockTime = request.getUnblockTime();

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        BlockedSession blockedSession = blockedSessionRepository.findAndCheckIfUserIsBlocked(user.get());
        if (blockedSession != null) {
            throw new InvalidateParamsException("Người dùng đã bị khóa");
        }

        BlockedSession newBlockedSession = new BlockedSession();
        newBlockedSession.setBlocker(manager);
        newBlockedSession.setBlockedUser(user.get());
        newBlockedSession.setReason(reason);
        newBlockedSession.setBlockTime(System.currentTimeMillis());
        newBlockedSession.setUnblockTime(unblockTime);
        newBlockedSession.setIsUnblocked(false);
        blockedSessionRepository.save(newBlockedSession);

        Logger.printf("Block user successfully with username: " + user.get().getUserName());
    }

    @Override
    public void unblockUser(Long userId, HttpServletRequest httpRequest) {

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        BlockedSession blockedSession = blockedSessionRepository.findAndCheckIfUserIsBlocked(user.get());

        if (blockedSession == null) {
            throw new InvalidateParamsException("Người dùng chưa bị khóa");
        }

        blockedSession.setIsUnblocked(true);
        blockedSessionRepository.save(blockedSession);

        Logger.printf("Unblock user successfully with username: " + user.get().getUserName());
    }

    @Override
    public List<ModeratorPermissionDto> fetchModeratorPermission(Long userId, HttpServletRequest httpRequest) {

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        User moderator = userRepository.findById(userId).orElse(null);

        if (moderator == null) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        if (moderator.getRole() != Role.MODERATOR) {
            throw new InvalidateParamsException("Người dùng không phải là quản trị viên");
        }

        List<ModeratorPermissionDto> data = new ArrayList<>();

        for (Permission permission : Permission.values()) {
            ModeratorPermissionDto item = new ModeratorPermissionDto();
            item.setKey(permission.getKey());
            item.setDescription(permission.getDescription());
            item.setGranted(moderatorPermissionRepository.isHasPermission(moderator.getUserName(), permission.getKey()));
            item.setChecked(item.getGranted());
            data.add(item);
        }

        return data;
    }

    @Override
    public void changeModeratorPermission(ChangeModeratorPermissionRequest request, HttpServletRequest httpRequest) {

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        User moderator = userRepository.findById(request.getUserId()).orElse(null);

        if (moderator == null) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        if (moderator.getRole() != Role.MODERATOR) {
            throw new InvalidateParamsException("Người dùng không phải là quản trị viên");
        }

        for (ModeratorPermissionDto item : request.getPermissions()) {
            if (item.getGranted() != item.getChecked()) {
                boolean isPermissionGranted = moderatorPermissionRepository.isHasPermission(moderator.getUserName(), item.getKey());
                if (item.getChecked()) {
                    if (isPermissionGranted) {
                        continue;
                    }
                    ModeratorPermission moderatorPermission = new ModeratorPermission();
                    moderatorPermission.setUserName(moderator.getUserName());
                    moderatorPermission.setPermission(item.getKey());
                    moderatorPermission.setCreatedAt(System.currentTimeMillis());
                    moderatorPermissionRepository.save(moderatorPermission);
                } else {
                    if (!isPermissionGranted) {
                        continue;
                    }
                    moderatorPermissionRepository.revokePermission(moderator.getUserName(), item.getKey());
                }
            }
        }
    }
}
