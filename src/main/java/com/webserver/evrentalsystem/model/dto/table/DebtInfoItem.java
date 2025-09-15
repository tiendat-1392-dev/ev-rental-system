package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebtInfoItem {
    private ContentItem<Long> debtId;
    private ContentItem<String> debtorUserName;
    private ContentItem<Long> owedDate;
    private ContentItem<String> creatorUserName;
    private ContentItem<Long> createdDate;
    private ContentItem<String> confirmedByUserName;
    private ContentItem<Long> confirmedDate;
    private ContentItem<Integer> amountOwed;
    private ContentItem<String> description;
}
