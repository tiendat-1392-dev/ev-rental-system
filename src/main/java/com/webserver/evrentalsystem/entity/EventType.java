package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum EventType {
    HT_ROAD("ht_road");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType fromValue(String value) {
        for (EventType eventType : EventType.values()) {
            if (eventType.value.equals(value)) {
                return eventType;
            }
        }
        return null;
    }
}
