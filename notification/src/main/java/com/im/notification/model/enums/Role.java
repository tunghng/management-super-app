package com.im.notification.model.enums;

import java.util.Arrays;

public enum Role {
    SYS_ADMIN,
    TENANT,
    MANAGER,
    CUSTOMER;

    public static Role lookup(final String id) {
        for (Role enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for account role type [%s]. " +
                "It should be %s", id, Arrays.asList(Role.values())));
    }
}
