package com.webserver.evrentalsystem.remoteconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Config {
    COIN_ICON_URL_FOR_TOP_UP_REQUEST("COIN_ICON_URL_FOR_TOP_UP_REQUEST", "https://i.imgur.com/63ci3X9.png", String.class, "URL coin icon cho yêu cầu nạp tiền"),
    REQUIRED_CITIZEN_ID("REQUIRED_CITIZEN_ID", "true", Boolean.class, "Yêu cầu thẻ căn cước");

    private String key;
    private String defaultValue;
    private Object type;
    private String description;
}
