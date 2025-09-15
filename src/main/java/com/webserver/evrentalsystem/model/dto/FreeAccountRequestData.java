package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FreeAccountRequestData {
    private String accountName;
    private String password;
    private Integer timeToUseInMinutes;
    private Long startTimeAllowedToLogin;
    private Long endTimeAllowedToLogin;
}
