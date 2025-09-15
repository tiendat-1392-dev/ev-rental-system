package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.Permission;
import com.webserver.evrentalsystem.entity.PlayedTime;
import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.dto.PlayedTimeRequestData;
import com.webserver.evrentalsystem.model.dto.UpdatePlayedTimeRequest;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.PlayedTimeItem;
import com.webserver.evrentalsystem.model.mapping.PlayedTimeMapping;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.PlayedTimeRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.ModeratorPlayedTimeManagementService;
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
public class ModeratorPlayedTimeManagementServiceImpl implements ModeratorPlayedTimeManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @Autowired
    private PlayedTimeRepository playedTimeRepository;

    @Override
    public PagingResponse<PlayedTimeItem> getPlayedTime(Long timestamp, String searchTerm, HttpServletRequest httpRequest) {

        if (timestamp == null || timestamp <= 0) {
            throw new InvalidateParamsException("Invalid timestamp");
        }

        Long startDate = DateUtils.getStartOfDay(timestamp);
        Long endDate = DateUtils.getEndOfDay(timestamp);

        if (searchTerm == null) {
            searchTerm = "";
        }

        ManagerValidation.validateManager(userRepository, httpRequest);

        List<PlayedTime> playedTimeList = playedTimeRepository.findByDateBetweenAndUserNameContaining(startDate, endDate, searchTerm);

        PagingResponse<PlayedTimeItem> data = new PagingResponse<>();
        data.setCurrentPage(1);
        data.setTotalPages(1);
        data.setTotalElements(playedTimeList.size());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Máy", false));
        headerItemList.add(new HeaderItem("Người dùng", false));
        headerItemList.add(new HeaderItem("Ngày", false));
        headerItemList.add(new HeaderItem("Thời gian", false));
        headerItemList.add(new HeaderItem("Thời gian chơi", false));

        data.setHeader(headerItemList);

        List<PlayedTimeItem> rows = new ArrayList<>();
        for (PlayedTime playedTime : playedTimeList) {
            rows.add(PlayedTimeMapping.toPlayedTimeItem(playedTime));
        }

        data.setRows(rows);
        data.setSearchTerm(searchTerm);

        return data;
    }

    @Override
    public void updatePlayedTime(UpdatePlayedTimeRequest requestBody, HttpServletRequest httpRequest) {

        if (!requestBody.isValid()) {
            throw new InvalidateParamsException("Invalid request body");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.UPDATE_PLAYED_TIME.getKey())) {
            throw new PermissionDeniedException("Bạn không có quyền thực hiện chức năng này");
        }

        Long startOfDay = DateUtils.getStartOfDay(requestBody.getDate());
        Long endOfDay = DateUtils.getEndOfDay(requestBody.getDate());

        playedTimeRepository.deleteByDateBetween(startOfDay, endOfDay);

        for (PlayedTimeRequestData playedTimeRequestData : requestBody.getData()) {

            if (playedTimeRequestData.getStartTime() < startOfDay || playedTimeRequestData.getStartTime() > endOfDay) {
                throw new InvalidateParamsException("Đã xảy ra lỗi khi cập nhật thời gian chơi.");
            }

            PlayedTime playedTime = PlayedTimeMapping.toPlayedTime(playedTimeRequestData);
            playedTime.setCreatedAt(System.currentTimeMillis());
            playedTime.setCreatedBy(manager.getUserName());
            playedTimeRepository.save(playedTime);
        }
    }
}
