package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.FreeAccount;
import com.webserver.evrentalsystem.model.dto.FreeAccountRequestData;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForGuest;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForModerator;
import com.webserver.evrentalsystem.utils.Formatter;

public class FreeAccountMapping {
    public static FreeAccountItemForModerator toFreeAccountItem(FreeAccount freeAccount) {
        FreeAccountItemForModerator item = new FreeAccountItemForModerator();
        item.setAccountName(new ContentItem<>(freeAccount.getAccountName(), freeAccount.getAccountName()));
        item.setPassword(new ContentItem<>(freeAccount.getPassword(), freeAccount.getPassword()));
        item.setTimeToUseInMinutes(
                new ContentItem<>(
                        freeAccount.getTimeToUseInMinutes() + " phút",
                    freeAccount.getTimeToUseInMinutes()
                )
        );
        item.setStartTimeAllowedToLogin(
                new ContentItem<>(
                    Formatter.formatTimestampToDateTimeString(freeAccount.getStartTimeAllowedToLogin(), "HH:mm"),
                    freeAccount.getStartTimeAllowedToLogin()
                )
        );
        item.setEndTimeAllowedToLogin(
                new ContentItem<>(
                    Formatter.formatTimestampToDateTimeString(freeAccount.getEndTimeAllowedToLogin(), "HH:mm"),
                    freeAccount.getEndTimeAllowedToLogin()
                )
        );
        return item;
    }

    public static FreeAccountItemForGuest toFreeAccountItemForGuest(FreeAccount freeAccount) {
        FreeAccountItemForGuest item = new FreeAccountItemForGuest();
        item.setAccountName(freeAccount.getAccountName());
        item.setPassword(freeAccount.getPassword());
        item.setTimeToUseInMinutes(freeAccount.getTimeToUseInMinutes());
        item.setStartTimeAllowedToLogin(freeAccount.getStartTimeAllowedToLogin());
        item.setEndTimeAllowedToLogin(freeAccount.getEndTimeAllowedToLogin());
        return item;
    }

    public static FreeAccount toFreeAccount(FreeAccountRequestData freeAccountRequestData) {
        FreeAccount freeAccount = new FreeAccount();
        freeAccount.setAccountName(freeAccountRequestData.getAccountName());
        freeAccount.setPassword(freeAccountRequestData.getPassword());
        freeAccount.setTimeToUseInMinutes(freeAccountRequestData.getTimeToUseInMinutes());
        freeAccount.setStartTimeAllowedToLogin(freeAccountRequestData.getStartTimeAllowedToLogin());
        freeAccount.setEndTimeAllowedToLogin(freeAccountRequestData.getEndTimeAllowedToLogin());
        return freeAccount;
    }
}
