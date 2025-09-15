package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum HTRoadEventRewardType {
    COIN("coin"),
    ITEM("item");

    private final String value;

    HTRoadEventRewardType(String value) {
        this.value = value;
    }

    public static HTRoadEventRewardType fromValue(String value) {
        for (HTRoadEventRewardType itemType : HTRoadEventRewardType.values()) {
            if (itemType.value.equals(value)) {
                return itemType;
            }
        }
        return null;
    }
}
