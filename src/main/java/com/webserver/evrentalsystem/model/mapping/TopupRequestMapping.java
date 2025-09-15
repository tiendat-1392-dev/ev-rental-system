package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.TopupRequest;
import com.webserver.evrentalsystem.model.dto.TopupRequestDtoForUser;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.TopupHistoryItem;
import com.webserver.evrentalsystem.model.dto.table.TopupRequestGroupItem;
import com.webserver.evrentalsystem.model.dto.table.TopupRequestItem;
import com.webserver.evrentalsystem.utils.Formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopupRequestMapping {
    public static List<TopupRequestGroupItem> toTopupRequestGroups(List<TopupRequest> topupRequests) {
        Map<String, TopupRequestGroupItem> groupMap = new HashMap<>();

        for (TopupRequest request : topupRequests) {
            String userName = request.getUser().getUserName();

            if (!groupMap.containsKey(userName)) {
                TopupRequestGroupItem groupItem = new TopupRequestGroupItem();
                groupItem.setUserName(userName);
                groupItem.setTopupRequests(new ArrayList<>());
                groupMap.put(userName, groupItem);
            }

            TopupRequestGroupItem groupItem = groupMap.get(userName);
            groupItem.getTopupRequests().add(toTopupRequestItem(request));
            groupItem.setTotalRequests(groupItem.getTotalRequests() + 1);
        }

        return new ArrayList<>(groupMap.values());
    }

    public static TopupRequestItem toTopupRequestItem(TopupRequest topupRequest) {
        TopupRequestItem item = new TopupRequestItem();
        item.setId(new ContentItem<>(String.valueOf(topupRequest.getId()), topupRequest.getId()));
        item.setAmount(new ContentItem<>(Formatter.formatCurrency(topupRequest.getAmount()), topupRequest.getAmount()));
        item.setCreatedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(topupRequest.getCreatedAt()), topupRequest.getCreatedAt()));
        return item;
    }

    public static List<TopupRequestDtoForUser> toListTopupRequestDtoForUser(List<TopupRequest> topupRequests, String iconUrl) {
        List<TopupRequestDtoForUser> result = new ArrayList<>();
        for (TopupRequest topupRequest : topupRequests) {
            result.add(toTopupRequestDtoForUser(topupRequest, iconUrl));
        }
        return result;
    }

    public static TopupRequestDtoForUser toTopupRequestDtoForUser(TopupRequest topupRequest, String iconUrl) {
        return new TopupRequestDtoForUser(
                topupRequest.getId(),
                iconUrl,
                topupRequest.getAmount(),
                topupRequest.getCreatedAt()
        );
    }

    public static TopupHistoryItem toTopupHistoryItem(TopupRequest topupRequest) {
        TopupHistoryItem item = new TopupHistoryItem();
        item.setId(new ContentItem<>(String.valueOf(topupRequest.getId()), topupRequest.getId()));
        item.setUserName(new ContentItem<>(topupRequest.getUser().getUserName(), topupRequest.getUser().getUserName()));
        item.setAmount(new ContentItem<>(Formatter.formatCurrency(topupRequest.getAmount()), topupRequest.getAmount()));

        if (topupRequest.getApprovedBy() != null) {
            item.setApprovedBy(new ContentItem<>(topupRequest.getApprovedBy().getUserName(), topupRequest.getApprovedBy().getUserName()));
        } else {
            item.setApprovedBy(new ContentItem<>("", ""));
        }

        if (topupRequest.getApprovedAt() != null) {
            item.setApprovedAt(new ContentItem<>(Formatter.formatTimestampToDateTimeString(topupRequest.getApprovedAt()), topupRequest.getApprovedAt()));
        } else {
            item.setApprovedAt(new ContentItem<>("", 0L));
        }

        if (topupRequest.getRejectedBy() != null) {
            item.setRejectedBy(new ContentItem<>(topupRequest.getRejectedBy().getUserName(), topupRequest.getRejectedBy().getUserName()));
        } else {
            item.setRejectedBy(new ContentItem<>("", ""));
        }

        if (topupRequest.getRejectedAt() != null) {
            item.setRejectedAt(new ContentItem<>(Formatter.formatTimestampToDateTimeString(topupRequest.getRejectedAt()), topupRequest.getRejectedAt()));
        } else {
            item.setRejectedAt(new ContentItem<>("", 0L));
        }

        if (topupRequest.getReason() != null) {
            item.setReason(new ContentItem<>(topupRequest.getReason(), topupRequest.getReason()));
        } else {
            item.setReason(new ContentItem<>("", ""));
        }

        item.setCreatedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(topupRequest.getCreatedAt()), topupRequest.getCreatedAt()));
        return item;
    }
}
