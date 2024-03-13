package com.im.support.model.enums;

import java.util.Arrays;

public enum TicketState {
    OPEN,
    PROCESSING,
    CLOSED;

    public static TicketState lookup(final String id) {
        for (TicketState enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for ticket state [%s]. " +
                "It should be %s", id, Arrays.asList(TicketState.values())));
    }
}