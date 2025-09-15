package com.webserver.evrentalsystem.model;

import lombok.Getter;

@Getter
public enum TopupRequestHistoryFilterByStatus {
    APPROVED("approved"),
    REJECTED("rejected");

    private final String value;

    TopupRequestHistoryFilterByStatus(String value) {
        this.value = value;
    }
}
