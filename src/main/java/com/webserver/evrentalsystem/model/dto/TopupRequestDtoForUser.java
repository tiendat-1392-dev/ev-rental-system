package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopupRequestDtoForUser {
    private Long id;
    private String iconUrl;
    private Integer amount;
    private Long createdDate;
}
