package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FreeAccountItemForGuest {
    private String accountName;
    private String password;
    private Integer timeToUseInMinutes;
    private Long startTimeAllowedToLogin;
    private Long endTimeAllowedToLogin;
}
