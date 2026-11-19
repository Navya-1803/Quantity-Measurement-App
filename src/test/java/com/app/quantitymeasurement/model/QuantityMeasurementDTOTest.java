package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.WeightUnit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDTOTest {

    private QuantityMeasurementEntity sampleEntity() {
        QuantityModel<IMeasurable> kg = new QuantityModel<>(1.0, WeightUnit.KILOGRAM);
        QuantityModel<IMeasurable> g = new QuantityModel<>(1000.0, WeightUnit.GRAM);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(kg, g, "add", new QuantityModel<IMeasurable>(2.0, WeightUnit.KILOGRAM));
        entity.setId(7L);
        return entity;
    }

    @Test
    void testFromEntity_CopiesAllFields() {
        QuantityMeasurementDTO dto = QuantityMeasurementDTO.fromEntity(sampleEntity());
        assertEquals(1.0, dto.getThisValue());
        assertEquals("KILOGRAM", dto.getThisUnit());
        assertEquals(1000.0, dto.getThatValue());
        assertEquals("add", dto.getOperation());
        assertEquals(2.0, dto.getResultValue());
        assertEquals("WeightUnit", dto.getResultMeasurementType());
        assertFalse(dto.isError());
    }

    @Test
    void testFromEntity_Null() {
        assertNull(QuantityMeasurementDTO.fromEntity(null));
    }

    @Test
    void testFromEntityList() {
        List<QuantityMeasurementDTO> dtos = QuantityMeasurementDTO.fromEntityList(List.of(sampleEntity(), sampleEntity()));
        assertEquals(2, dtos.size());
    }

    @Test
    void testToEntity_RoundTrip() {
        QuantityMeasurementEntity original = sampleEntity();
        QuantityMeasurementEntity copy = QuantityMeasurementDTO.fromEntity(original).toEntity();
        assertNull(copy.getId());
        assertEquals(original.getResultUnit(), copy.getResultUnit());
        assertEquals(original.getThatValue(), copy.getThatValue());
    }

    @Test
    void testBuilder() {
        QuantityMeasurementDTO dto = QuantityMeasurementDTO.builder().operation("compare").resultString("true").build();
        assertEquals("compare", dto.getOperation());
        assertEquals("true", dto.getResultString());
    }
}
