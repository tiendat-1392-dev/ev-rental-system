package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserInfoRequest {
    private String realName;
    private Long dateOfBirth;
    private String gender;
    private String address;
    private String citizenIdentityCard;

    public boolean isValid() {
        return realName != null && dateOfBirth != null && gender != null && address != null;
    }
}
