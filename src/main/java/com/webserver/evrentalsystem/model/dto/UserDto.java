package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String userName;
    private String userPublicName;
    private String avatar;
    private String role;
    private Integer amount;
    private Integer luckyWheelSpin;
    private String membershipClass;
    private String realName;
    private String citizenIdentityCard;
    private Long dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private Integer amountOwed;
    private Long createdDate;
}
