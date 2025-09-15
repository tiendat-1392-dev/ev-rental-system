package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.ItemType;
import jakarta.persistence.AttributeConverter;

public class ItemTypeJpaConverter implements AttributeConverter<ItemType, String> {
    @Override
    public String convertToDatabaseColumn(ItemType itemType) {
        if (itemType == null) {
            return null;
        }
        return itemType.getValue();
    }

    @Override
    public ItemType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return ItemType.fromValue(s);
    }
}
