package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDataItem {
    private ContentItem<Long> userId;
    private ContentItem<Long> disabledSessionId;
    private ContentItem<String> userName;
    private ContentItem<String> userPublicName;
    private ContentItem<Integer> amount;
    private ContentItem<String> status;
    private ContentItem<Long> createdDate;
}
