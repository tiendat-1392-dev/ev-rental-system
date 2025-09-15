package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum MembershipClass {
    NEWBIE("newbie", "Hội viên mới");

    private final String key;
    private final String value;

    MembershipClass(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MembershipClass fromKey(String key) {
        for (MembershipClass membershipClass : MembershipClass.values()) {
            if (membershipClass.key.equals(key)) {
                return membershipClass;
            }
        }
        return null;
    }
}
