package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDebtRequest {
    private Long debtId;
    private Integer owedAmount;
    private String description;
    private Long owedDate;

    public boolean isValid() {
        return debtId != null && owedAmount != null && description != null && owedDate != null;
    }
}
