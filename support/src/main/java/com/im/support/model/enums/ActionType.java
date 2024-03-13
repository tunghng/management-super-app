package com.im.support.model.enums;

import java.util.Arrays;

public enum ActionType {
    CREATED,
    DELETED,
    UPDATED,
    ATTRIBUTES_UPDATED;

    public static ActionType lookup(final String id) {
        for (ActionType enumValues : ActionType.values()) {
            if (enumValues.name().equalsIgnoreCase(id)) {
                return enumValues;
            }
        }
        throw new RuntimeException(String.format("Invalid value for action type [%s]. " +
                "It should be %s", id, Arrays.asList(PriorityType.values())));
    }
}
