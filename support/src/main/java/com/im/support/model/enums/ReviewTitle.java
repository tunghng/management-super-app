package com.im.support.model.enums;

import java.util.Arrays;

public enum ReviewTitle {
    GOOD,
    BAD;

    public static ReviewTitle lookup(final String id) {
        for (ReviewTitle enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for review title [%s]. " +
                "It should be %s", id, Arrays.asList(ReviewTitle.values())));
    }
}
