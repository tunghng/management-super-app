package com.im.support.model.enums;

import java.util.Arrays;

public enum PriorityType {
    HIGH,
    MEDIUM,
    LOW;

    public static PriorityType lookup(final String id) {
        for (PriorityType enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for priority type [%s]. " +
                "It should be %s", id, Arrays.asList(PriorityType.values())));
    }
}
