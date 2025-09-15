package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayedTimeRequestData {
    private String computerName;
    private String userName;
    private Long startTime;
    private Integer playedTime;
}
