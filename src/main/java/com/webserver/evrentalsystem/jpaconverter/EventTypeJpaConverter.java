package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.EventType;
import jakarta.persistence.AttributeConverter;

public class EventTypeJpaConverter implements AttributeConverter<EventType, String> {
    @Override
    public String convertToDatabaseColumn(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return eventType.getValue();
    }

    @Override
    public EventType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return EventType.fromValue(s);
    }
}
