package com.webserver.evrentalsystem;

import com.webserver.evrentalsystem.entity.BlockedSession;
import com.webserver.evrentalsystem.entity.MembershipClass;
import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.repository.BlockedSessionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class EVRentalSystemWebServerApplication extends SpringBootServletInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockedSessionRepository blockedSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EVRentalSystemWebServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(EVRentalSystemWebServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        // setupAdminUser();
    }

    private void setupAdminUser() {
        List<User> adminUsers = userRepository.findAllByRole(Role.ADMIN);

        boolean shouldCreateNewAdmin = true;

        for (User adminUser : adminUsers) {

            BlockedSession blockedSession = blockedSessionRepository.findAndCheckIfUserIsBlocked(adminUser);

            if (adminUser.getUserName().equals(adminUsername)) {
                shouldCreateNewAdmin = false;
                // đổi mật khẩu và unblock tài khoản
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRefreshToken(null);
                userRepository.save(adminUser);
                Logger.printf("Đổi mật khẩu và force đăng xuất tài khoản admin " + adminUser.getUserName() + " thành công!");

                if (blockedSession != null) {
                    blockedSession.setIsUnblocked(true);
                    blockedSessionRepository.save(blockedSession);
                    Logger.printf("Mở khóa tài khoản admin " + adminUser.getUserName() + " thành công!");
                }
            } else {
                // block tài khoản
                if (blockedSession == null) {
                    BlockedSession newBlockedSession = new BlockedSession();
                    newBlockedSession.setBlocker(adminUser);
                    newBlockedSession.setBlockedUser(adminUser);
                    newBlockedSession.setReason("system");
                    newBlockedSession.setBlockTime(System.currentTimeMillis());
                    newBlockedSession.setIsUnblocked(false);
                    blockedSessionRepository.save(newBlockedSession);
                    Logger.printf("Khóa tài khoản admin " + adminUser.getUserName() + " thành công!");
                }
            }
        }

        if (shouldCreateNewAdmin) {
            User adminUser = new User();
            adminUser.setUserName(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(Role.ADMIN);
            adminUser.setAmount(0);
            adminUser.setMembershipClass(MembershipClass.NEWBIE);
            adminUser.setCreatedAt(System.currentTimeMillis());
            adminUser.setCreatedBy("system");
            userRepository.save(adminUser);
            Logger.printf("Tạo tài khoản admin " + adminUser.getUserName() + " thành công!");
        }
    }
}
