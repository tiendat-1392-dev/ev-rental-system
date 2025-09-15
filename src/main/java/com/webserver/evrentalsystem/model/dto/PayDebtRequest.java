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
public class PayDebtRequest {
    Long userId;
    List<Long> debtIds;

    public boolean isValid() {
        return userId != null && debtIds != null && !debtIds.isEmpty();
    }
}
