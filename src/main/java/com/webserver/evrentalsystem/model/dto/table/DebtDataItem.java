package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DebtDataItem {
    private Long debtId;
    private String creditorUsername;
    private String owedDate;
    private Integer amountOwed;
    private String description;
}
