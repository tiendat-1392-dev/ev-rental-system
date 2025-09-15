package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.HTRoadEventRewardType;
import jakarta.persistence.AttributeConverter;

public class HTRoadEventRewardTypeJpaConverter implements AttributeConverter<HTRoadEventRewardType, String> {
    @Override
    public String convertToDatabaseColumn(HTRoadEventRewardType rewardType) {
        if (rewardType == null) {
            return null;
        }
        return rewardType.getValue();
    }

    @Override
    public HTRoadEventRewardType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return HTRoadEventRewardType.fromValue(s);
    }
}
