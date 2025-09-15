package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum ItemType {
    VOUCHER("voucher"),
    LUCKY_WHEEL("luckyWheel");

    private final String value;

    ItemType(String value) {
        this.value = value;
    }

    public static ItemType fromValue(String value) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.value.equals(value)) {
                return itemType;
            }
        }
        return null;
    }
}
