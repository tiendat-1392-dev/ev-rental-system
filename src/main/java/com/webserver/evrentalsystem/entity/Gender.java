package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
            }
        }
        return null;
    }
}
