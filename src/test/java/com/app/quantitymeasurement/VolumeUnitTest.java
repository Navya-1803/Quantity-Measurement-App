package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VolumeUnitTest {

    private static final double DELTA = 1e-6;

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), DELTA);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), DELTA);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), DELTA);
    }

    @Test
    void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(0.264172, VolumeUnit.GALLON.convertFromBaseUnit(1.0), DELTA);
    }

    @Test
    void testGetUnitName() {
        assertEquals("GALLON", VolumeUnit.GALLON.getUnitName());
    }

    @Test
    void testGetUnitInstance_ValidName() {
        assertEquals(VolumeUnit.GALLON, VolumeUnit.GALLON.getUnitInstance("gallon"));
    }

    @Test
    void testGetUnitInstance_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> VolumeUnit.GALLON.getUnitInstance("UNKNOWN"));
    }

    @Test
    void testGetMeasurementType() {
        assertEquals("VolumeUnit", VolumeUnit.GALLON.getMeasurementType());
    }
}
