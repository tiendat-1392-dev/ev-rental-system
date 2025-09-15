package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePlayedTimeRequest {
    private Long date;
    private List<PlayedTimeRequestData> data;

    public boolean isValid() {
        return date != null && data != null && !data.isEmpty();
    }
}
