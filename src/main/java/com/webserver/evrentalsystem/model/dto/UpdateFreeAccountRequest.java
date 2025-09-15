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
public class UpdateFreeAccountRequest {
    private Long date;
    private List<FreeAccountRequestData> data;

    public boolean isValid() {
        return date != null && data != null && !data.isEmpty();
    }
}
