package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementEntityTest {

    private final QuantityModel<IMeasurable> oneFoot = new QuantityModel<>(1.0, LengthUnit.FEET);
    private final QuantityModel<IMeasurable> twelveInches = new QuantityModel<>(12.0, LengthUnit.INCHES);

    @Test
    void testQuantityEntity_SingleOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, "convert", twelveInches);
        assertEquals("FEET", entity.getThisUnit());
        assertEquals("convert", entity.getOperation());
        assertEquals(12.0, entity.getResultValue());
        assertEquals("INCHES", entity.getResultUnit());
        assertNull(entity.getThatUnit());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, twelveInches, "compare", "true");
        assertEquals(12.0, entity.getThatValue());
        assertEquals("LengthUnit", entity.getThatMeasurementType());
        assertEquals("true", entity.getResultString());
        assertFalse(entity.isError());
    }

    @Test
    void testQuantityEntity_QuantityResultConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, twelveInches, "add",
                new QuantityModel<IMeasurable>(2.0, LengthUnit.FEET));
        assertEquals(2.0, entity.getResultValue());
        assertEquals("LengthUnit", entity.getResultMeasurementType());
    }

    @Test
    void testQuantityEntity_ScalarResultConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, twelveInches, "divide", 1.0);
        assertEquals(1.0, entity.getResultValue());
        assertNull(entity.getResultUnit());
    }

    @Test
    void testQuantityEntity_ErrorConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, null, "add", "failed", true);
        assertTrue(entity.isError());
        assertEquals("failed", entity.getErrorMessage());
        assertNull(entity.getThatUnit());
    }

    @Test
    void testLifecycleCallbacks_SetTimestamps() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, "convert", twelveInches);
        entity.onCreate();
        assertNotNull(entity.getCreatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
        entity.onUpdate();
        assertFalse(entity.getUpdatedAt().isBefore(entity.getCreatedAt()));
    }

    @Test
    void testLombokGeneratedMethods() {
        QuantityMeasurementEntity first = new QuantityMeasurementEntity(oneFoot, twelveInches, "compare", "true");
        QuantityMeasurementEntity second = new QuantityMeasurementEntity(oneFoot, twelveInches, "compare", "true");
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("operation=compare"));
    }
}
