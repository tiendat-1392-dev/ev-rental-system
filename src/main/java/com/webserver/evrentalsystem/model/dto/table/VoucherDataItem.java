package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VoucherDataItem {
    private ContentItem<Long> id;
    private ContentItem<String> name;
    private ContentItem<Integer> maxDiscount;
    private ContentItem<Double> discountFactor;
    private ContentItem<Integer> minTopup;
    private ContentItem<Long> expiredAt;
    private ContentItem<Boolean> isActive;
    private ContentItem<Boolean> canUse;
    private ContentItem<String> description;
    private ContentItem<String> imageUrl;
    private ContentItem<Long> createdAt;
}
