package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewDebtRequest {
    private Long userId;
    private Integer amountOwed;
    private Long owedDate;
    private String description;

    public boolean isValid() {
        return userId != null && amountOwed != null && owedDate != null && amountOwed > 0;
    }
}
