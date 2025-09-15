package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.exception.UserAlreadyExistsException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.*;
import com.webserver.evrentalsystem.model.dto.*;
import com.webserver.evrentalsystem.model.dto.table.*;
import com.webserver.evrentalsystem.model.mapping.DebtMapping;
import com.webserver.evrentalsystem.model.mapping.UserMapping;
import com.webserver.evrentalsystem.remoteconfig.RemoteConfigUtils;
import com.webserver.evrentalsystem.repository.BlockedSessionRepository;
import com.webserver.evrentalsystem.repository.DebtRepository;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.ModeratorAccountManagementService;
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
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ModeratorAccountManagementServiceImpl implements ModeratorAccountManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @Autowired
    private BlockedSessionRepository blockedSessionRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserMapping userMapping;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RemoteConfigUtils remoteConfigUtils;

    @Override
    public void createNewUserAccount(CreateNewUserAccountRequest requestBody, HttpServletRequest httpRequest) {

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        Logger.printf("Role: " + manager.getRole());

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.CREATE_NEW_USER_ACCOUNT.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền tạo tài khoản người dùng");
            throw new PermissionDeniedException("Bạn không có quyền tạo tài khoản người dùng");
        }

        String userName = requestBody.getUserName();
        String password = requestBody.getPassword();

//        String citizenIdentityCard = requestBody.getCitizenIdentityCard();
//
//        // kiểm tra xem nếu bắt buộc phải có citizenIdentityCard thì phải có citizenIdentityCard
//        boolean isRequiredCitizenId = remoteConfigUtils.getAppConfig(Config.REQUIRED_CITIZEN_ID, Boolean.class);
//
//        if (isRequiredCitizenId && ( citizenIdentityCard == null || citizenIdentityCard.isEmpty() )) {
//            Logger.printf("Yêu cầu thẻ căn cước để đăng ký tài khoản");
//            throw new InvalidateParamsException("Yêu cầu thẻ căn cước để đăng ký tài khoản");
//        }
//
//        if (userName.isEmpty() || password.isEmpty()) {
//            throw new InvalidateParamsException("Tên đăng nhập và mật khẩu không được để trống");
//        }

        User user = userRepository.findByUserName(userName);

        if (user != null) {
            throw new UserAlreadyExistsException("Người dùng đã tồn tại!");
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        // newUser.setCitizenIdentityCard(citizenIdentityCard);
        newUser.setRole(Role.USER);
        newUser.setMembershipClass(MembershipClass.NEWBIE);
        newUser.setAmount(0);
        newUser.setCreatedAt(System.currentTimeMillis());
        newUser.setCreatedBy(manager.getUserName());

        userRepository.save(newUser);

        Logger.printf("Create new user account successfully with username: " + userName);
    }

    @Override
    public PagingResponse<UserDataItem> getUsers(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpRequest) {

        if (pageNo < 1 || pageSize < 1) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        ManagerValidation.validateManager(userRepository, httpRequest);

        Page<User> page = userRepository.findAllByRoleIn(Role.USER, searchTerm, PageRequest.of(Math.max((pageNo - 1), 0), pageSize));

        Logger.printf("Total elements: " + page.getTotalElements());

        PagingResponse<UserDataItem> data = new PagingResponse<>();

        data.setCurrentPage(pageNo);
        data.setTotalPages(page.getTotalPages());
        data.setTotalElements(page.getTotalElements());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", true));
        headerItemList.add(new HeaderItem("disabledSessionId", true));
        headerItemList.add(new HeaderItem("Tên đăng nhập", false));
        headerItemList.add(new HeaderItem("Tên công khai", false));
        headerItemList.add(new HeaderItem("Số dư", false));
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
        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.CHANGE_USER_PASSWORD.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền thay đổi mật khẩu người dùng");
            throw new PermissionDeniedException("Bạn không có quyền thay đổi mật khẩu người dùng");
        }

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

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.BLOCK_USER.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền khóa người dùng");
            throw new PermissionDeniedException("Bạn không có quyền khóa người dùng");
        }

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

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.BLOCK_USER.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền mở khóa người dùng");
            throw new PermissionDeniedException("Bạn không có quyền mở khóa người dùng");
        }

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
    public UserDto getUserInfo(Long userId, HttpServletRequest httpRequest) {
        ManagerValidation.validateManager(userRepository, httpRequest);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        Logger.printf("userName : " + user.get().getUserName());
        Logger.printf("real: " + user.get().getRealName());

        return userMapping.toUserDto(user.get());
    }

    @Override
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request, HttpServletRequest httpRequest) {
        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

//        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.UPDATE_USER_INFO.getKey())) {
//            Logger.printf("Moderator " + manager.getUserName() + " không có quyền cập nhật thông tin người dùng");
//            throw new PermissionDeniedException("Bạn không có quyền cập nhật thông tin người dùng");
//        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        User userToUpdate = user.get();

        if (!request.isValid()) {
            throw new InvalidateParamsException("Thông tin không đủ");
        }

        Logger.printf("user : " + userToUpdate.getUserName());
        Logger.printf("setRealName: " + request.getRealName());

        userToUpdate.setRealName(request.getRealName());
        userToUpdate.setDateOfBirth(request.getDateOfBirth());
        userToUpdate.setGender(Gender.fromValue(request.getGender()));
        userToUpdate.setAddress(request.getAddress());

        if (request.getCitizenIdentityCard() != null) {
            userToUpdate.setCitizenIdentityCard(request.getCitizenIdentityCard());
        }

        userRepository.save(userToUpdate);

        Logger.printf("Update user info successfully with username: " + user.get().getUserName());
    }

    @Override
    public TableDataResponse<DebtDataItem> getAllDebtsByUser(Long userId, HttpServletRequest httpRequest) {
        ManagerValidation.validateManager(userRepository, httpRequest);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        List<Debt> debts = debtRepository.findAllUnpaidDebtsByDebtor(user.get());

        TableDataResponse<DebtDataItem> data = new TableDataResponse<>();

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", true));
        headerItemList.add(new HeaderItem("Nhân viên", false));
        headerItemList.add(new HeaderItem("Ngày nợ", false));
        headerItemList.add(new HeaderItem("Số tiền", false));
        headerItemList.add(new HeaderItem("Thông tin thêm", false));

        data.setHeader(headerItemList);

        List<DebtDataItem> debtDataItemList = new ArrayList<>();
        for (Debt debt : debts) {
            debtDataItemList.add(DebtMapping.toDebtDataItem(debt));
        }

        data.setRows(debtDataItemList);

        return data;
    }

    @Override
    public void createNewDebt(CreateNewDebtRequest request, HttpServletRequest httpRequest) {
        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

//        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.CREATE_NEW_DEBT.getKey())) {
//            Logger.printf("Moderator " + manager.getUserName() + " không có quyền tạo nợ");
//            throw new PermissionDeniedException("Bạn không có quyền tạo nợ");
//        }

        if (!request.isValid()) {
            throw new InvalidateParamsException("Thông tin không đủ");
        }

        Optional<User> user = userRepository.findById(request.getUserId());

        if (user.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        Debt debt = new Debt();
        debt.setDebtor(user.get());
        debt.setCreditor(manager);
        debt.setAmountOwed(request.getAmountOwed());
        debt.setOwedDate(request.getOwedDate());
        debt.setDescription(request.getDescription());
        debt.setIsPaid(false);
        debt.setIsDeleted(false);
        debt.setCreatedAt(System.currentTimeMillis());

        debtRepository.save(debt);

        Logger.printf("Create new debt successfully with username: " + user.get().getUserName());
    }

    @Override
    public void confirmDebtPayment(PayDebtRequest request, HttpServletRequest httpRequest) {
        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

//        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.CONFIRM_DEBT_PAYMENT.getKey())) {
//            Logger.printf("Moderator " + manager.getUserName() + " không có quyền xác nhận thanh toán nợ");
//            throw new PermissionDeniedException("Bạn không có quyền xác nhận thanh toán nợ");
//        }

        if (!request.isValid()) {
            throw new InvalidateParamsException("Thông tin không đủ");
        }

        Optional<User> debtor = userRepository.findById(request.getUserId());

        if (debtor.isEmpty()) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        // set đã trả = true, cập nhật thông tin người xác nhận
        List<Long> debtIds = request.getDebtIds();

        for (Long debtId : debtIds) {
            Optional<Debt> debt = debtRepository.findById(debtId);

            if (debt.isEmpty()) {
                throw new InvalidateParamsException("Không tìm thấy nợ");
            }

            if (!Objects.equals(debt.get().getDebtor().getId(), debtor.get().getId())) {
                throw new InvalidateParamsException("Nợ không thuộc về người dùng này");
            }

            if (debt.get().getIsPaid()) {
                throw new InvalidateParamsException("Nợ đã được thanh toán rồi");
            }

            debt.get().setIsPaid(true);
            debt.get().setPaidDate(System.currentTimeMillis());
            debt.get().setConfirmedPaymentBy(manager);
            debtRepository.save(debt.get());
        }
    }
}
