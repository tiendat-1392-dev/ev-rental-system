package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.Gender;
import jakarta.persistence.AttributeConverter;

public class GenderJpaConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Gender.fromValue(s);
    }
}
