package com.webserver.evrentalsystem.model;

import lombok.Getter;

@Getter
public enum TopupRequestHistoryFilterByUser {
    USER("user"),
    STAFF("staff");

    private final String value;

    TopupRequestHistoryFilterByUser(String value) {
        this.value = value;
    }
}
