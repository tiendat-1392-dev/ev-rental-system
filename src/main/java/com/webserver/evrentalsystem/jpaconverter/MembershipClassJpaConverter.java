package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.MembershipClass;
import jakarta.persistence.AttributeConverter;

public class MembershipClassJpaConverter implements AttributeConverter<MembershipClass, String> {
    @Override
    public String convertToDatabaseColumn(MembershipClass membershipClass) {
        if (membershipClass == null) {
            return null;
        }
        return membershipClass.getKey();
    }

    @Override
    public MembershipClass convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return MembershipClass.fromKey(s);
    }
}
