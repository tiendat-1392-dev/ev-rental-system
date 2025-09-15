package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.TopupRequest;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.model.dto.TopupRequestDtoForUser;
import com.webserver.evrentalsystem.model.dto.UserDto;
import com.webserver.evrentalsystem.model.mapping.TopupRequestMapping;
import com.webserver.evrentalsystem.model.mapping.UserMapping;
import com.webserver.evrentalsystem.remoteconfig.Config;
import com.webserver.evrentalsystem.remoteconfig.RemoteConfigUtils;
import com.webserver.evrentalsystem.repository.TopupRequestRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.UserService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopupRequestRepository topupRequestRepository;

    @Autowired
    private UserMapping userMapping;

    @Autowired
    private RemoteConfigUtils remoteConfigUtils;

    @Override
    public UserDto getUserInfo(HttpServletRequest request) {
        User user = UserValidation.validateUser(userRepository, request);
        return userMapping.toUserDto(user);
    }

    @Override
    public List<TopupRequestDtoForUser> getTopupRequests(HttpServletRequest request) {
        User user = UserValidation.validateUser(userRepository, request);

        List<TopupRequest> topupRequests = topupRequestRepository.findAllPendingTopupRequestByUserName(user.getUserName());
        String coinIconUrl = remoteConfigUtils.getAppConfig(Config.COIN_ICON_URL_FOR_TOP_UP_REQUEST, String.class);

        return TopupRequestMapping.toListTopupRequestDtoForUser(topupRequests, coinIconUrl);
    }

    @Override
    public void createTopupRequest(HttpServletRequest request, Integer amount) {
        User user = UserValidation.validateUser(userRepository, request);

        if (Objects.isNull(amount) || amount <= 0 || amount > 10000000) {
            throw new InvalidateParamsException("Số tiền không hợp lệ");
        }

        // check if user has enough money
        if (user.getAmount() < amount) {
            throw new InvalidateParamsException("Số dư không đủ");
        }

        // update user amount
        user.setAmount(user.getAmount() - amount);
        userRepository.save(user);

        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setUser(user);
        topupRequest.setAmount(amount);
        topupRequest.setCreatedAt(System.currentTimeMillis());
        topupRequest.setIsApproved(false);
        topupRequest.setIsRejected(false);
        topupRequest.setIsDeleted(false);
        topupRequestRepository.save(topupRequest);
    }
}
