package com.webserver.evrentalsystem.model;

import lombok.Getter;

@Getter
public enum DebtFilterByUser {
    DEBTOR("debtor"),
    CREATOR("creator"),
    CONFIRMED_BY_USER("confirmedByUser");

    private final String value;

    DebtFilterByUser(String value) {
        this.value = value;
    }
}
