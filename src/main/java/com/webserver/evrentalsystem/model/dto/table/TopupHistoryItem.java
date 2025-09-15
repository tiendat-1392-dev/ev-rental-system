package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopupHistoryItem {
    private ContentItem<Long> id;
    private ContentItem<String> userName;
    private ContentItem<Integer> amount;
    private ContentItem<String> approvedBy;
    private ContentItem<Long> approvedAt;
    private ContentItem<String> rejectedBy;
    private ContentItem<Long> rejectedAt;
    private ContentItem<String> reason;
    private ContentItem<Long> createdDate;
}
