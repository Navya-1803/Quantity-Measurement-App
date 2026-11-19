package com.app.quantitymeasurement.model;

import java.util.Arrays;
import java.util.List;

/**
 * OperationType lists the operations that can be performed on quantities.
 * The display name is the value stored in the database.
 *
 * @author Navya Raj
 */
public enum OperationType {

    COMPARE("compare"),
    CONVERT("convert"),
    ADD("add"),
    SUBTRACT("subtract"),
    DIVIDE("divide");

    private final String displayName;

    OperationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds an operation by name, ignoring case, e.g. "ADD" or "add".
     *
     * @throws IllegalArgumentException if the name is not a known operation
     */
    public static OperationType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation: " + value
                        + ". Valid operations are " + getValidOperations()));
    }

    public static List<String> getValidOperations() {
        return Arrays.stream(values()).map(Enum::name).toList();
    }
}
