package com.im.form.model.enums;

import java.util.Arrays;

public enum Direction {
    ASC, DESC;

    public static Direction lookup(final String id) {
        for (Direction enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for type [%s]. " +
                "It should be %s", id, Arrays.asList(values())));
    }
}

