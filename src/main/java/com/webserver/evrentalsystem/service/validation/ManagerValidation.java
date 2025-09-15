package com.webserver.evrentalsystem.service.validation;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.servlet.http.HttpServletRequest;

public class ManagerValidation {
    public static User validateManager(UserRepository userRepository, HttpServletRequest httpRequest) {

        String managerUserName = (String) httpRequest.getAttribute("userName");
        String role = (String) httpRequest.getAttribute("role");

        Logger.printf("Manager username: " + managerUserName);
        Logger.printf("Role: " + role);

        if (managerUserName == null || role == null) {
            Logger.printf("Không thể xác thực được người dùng");
            throw new InvalidateParamsException("Không thể xác thực được người dùng");
        }

        User manager = userRepository.findByUserName(managerUserName);

        if (manager == null) {
            Logger.printf("Không tìm thấy người dùng");
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        if (!role.equals(manager.getRole().getValue())) {
            Logger.printf("Quyền không khớp");
            throw new PermissionDeniedException("Đăng nhập lại để fix lỗi này");
        }

        if (manager.getRole() != Role.MODERATOR && manager.getRole() != Role.ADMIN) {
            Logger.printf("Người dùng không có quyền thực hiện hành động này");
            throw new PermissionDeniedException("Không có quyền thực hiện hành động này");
        }

        return manager;
    }

    public static User validateAdmin(UserRepository userRepository, HttpServletRequest httpRequest) {
        User manager = validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN) {
            throw new PermissionDeniedException("Không có quyền thực hiện hành động này");
        }

        return manager;
    }
}
