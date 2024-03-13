package com.im.support.model.enums;

import java.util.Arrays;

public enum EntityType {
    TICKET,
    TICKET_TYPE,
    REVIEW,
    FEEDBACK;

    public static EntityType lookup(final String id) {
        for (EntityType enumValues : EntityType.values()) {
            if (enumValues.name().equalsIgnoreCase(id)) {
                return enumValues;
            }
        }
        throw new RuntimeException(String.format("Invalid value for entity type [%s]. " +
                "It should be %s", id, Arrays.asList(PriorityType.values())));
    }
}
