package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopupRequestItem {
    private ContentItem<Long> id;
    private ContentItem<Integer> amount;
    private ContentItem<Long> createdDate;
}
