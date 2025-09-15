package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FreeAccountItemForModerator {
    private ContentItem<String> accountName;
    private ContentItem<String> password;
    private ContentItem<Integer> timeToUseInMinutes;
    private ContentItem<Long> startTimeAllowedToLogin;
    private ContentItem<Long> endTimeAllowedToLogin;
}
