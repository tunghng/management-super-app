package com.im.form.model.enums;

import java.util.Arrays;

public enum InputType {
    TEXT,
    RADIO,
    SELECT,
    ATTACHED_FILE,
    TIME_PICKER;

    public static InputType lookup(final String id) {
        for (InputType enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(id)) {
                return enumValue;
            }
        }
        throw new RuntimeException(String.format("Invalid value for input type [%s]. " +
                "It should be %s", id, Arrays.asList(InputType.values())));
    }
}
