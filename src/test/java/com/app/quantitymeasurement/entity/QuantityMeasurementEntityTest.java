package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        assertEquals(1.0, entity.getThisValue());
        assertEquals(12.0, entity.getThatValue());
        assertEquals("LengthUnit", entity.getThatMeasurementType());
        assertEquals("true", entity.getResultString());
        assertFalse(entity.isError());
    }

    @Test
    void testQuantityEntity_QuantityResultConstruction() {
        QuantityModel<IMeasurable> sum = new QuantityModel<>(2.0, LengthUnit.FEET);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, twelveInches, "add", sum);
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
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(oneFoot, null, "add", "add Error: failed", true);
        assertTrue(entity.isError());
        assertEquals("add Error: failed", entity.getErrorMessage());
        assertNull(entity.getThatUnit());
    }

    @Test
    void testQuantityEntity_ToString_Success() {
        String text = new QuantityMeasurementEntity(oneFoot, twelveInches, "compare", "true").toString();
        assertTrue(text.contains("operation=compare"));
        assertTrue(text.contains("result=true"));
    }

    @Test
    void testQuantityEntity_ToString_Error() {
        String text = new QuantityMeasurementEntity(oneFoot, twelveInches, "add", "boom", true).toString();
        assertTrue(text.contains("error=boom"));
    }

    @Test
    void testEntity_Immutability() {
        // no public setters exist, fields can only be set through constructors
        for (Method method : QuantityMeasurementEntity.class.getDeclaredMethods()) {
            if (method.getName().startsWith("set")) {
                assertTrue(Modifier.isPrivate(method.getModifiers()), method.getName() + " should be private");
            }
        }
    }

    @Test
    void testEntity_OperationType_Tracking() {
        assertEquals("subtract", new QuantityMeasurementEntity(oneFoot, twelveInches, "subtract", oneFoot).getOperation());
    }

    @Test
    void testEntity_IsSerializable() {
        assertInstanceOf(java.io.Serializable.class, new QuantityMeasurementEntity(oneFoot, "convert", twelveInches));
    }
}
