package com.im.form.model.enums;

import java.util.Arrays;

public enum Authority {
    SYS_ADMIN,
    TENANT_ADMIN,
    CUSTOMER_USER;

    public static Authority lookup(final String id) {
        for (Authority enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for authority type [%s]. " +
                "It should be %s", id, Arrays.asList(Authority.values())));
    }
}
