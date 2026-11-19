package com.app.quantitymeasurement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTypeTest {

    @Test
    void testDisplayNames() {
        assertEquals("compare", OperationType.COMPARE.getDisplayName());
        assertEquals("divide", OperationType.DIVIDE.getDisplayName());
    }

    @Test
    void testFromString_IgnoresCase() {
        assertEquals(OperationType.ADD, OperationType.fromString("add"));
        assertEquals(OperationType.SUBTRACT, OperationType.fromString(" SUBTRACT "));
    }

    @Test
    void testFromString_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> OperationType.fromString("multiply"));
        assertThrows(IllegalArgumentException.class, () -> OperationType.fromString(null));
    }

    @Test
    void testGetValidOperations() {
        assertEquals(5, OperationType.getValidOperations().size());
        assertTrue(OperationType.getValidOperations().contains("CONVERT"));
    }
}
