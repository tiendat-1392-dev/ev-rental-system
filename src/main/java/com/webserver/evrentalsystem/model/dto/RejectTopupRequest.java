package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RejectTopupRequest {
    private List<Long> topupRequestIds;
    private String reason;

    public boolean isValid() {
        return topupRequestIds != null && !topupRequestIds.isEmpty() && reason != null && !reason.isEmpty();
    }
}
