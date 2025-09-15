package com.webserver.evrentalsystem.model;

import lombok.Getter;

@Getter
public enum DebtFilterByDate {
    OWED_DATE("owedDate"),
    PAY_DATE("payDate"),
    IMPORTED_DATE("importedDate");

    private final String value;

    DebtFilterByDate(String value) {
        this.value = value;
    }
}
