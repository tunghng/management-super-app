package com.im.billing.model.enums;

import java.util.Arrays;

public enum BillState {
    UNPAID,
    PAID,
    CLOSED;

    public static BillState lookup(final String id) {
        for (BillState enumValue : values()) {
            if (enumValue.name().equals(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for ticket state [%s]. " +
                "It should be %s", id, Arrays.asList(BillState.values())));
    }
}
