package com.app.quantitymeasurement.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void testOpenApiInfo() {
        OpenAPI openAPI = new OpenApiConfig().quantityMeasurementOpenAPI();
        assertEquals("Quantity Measurement API", openAPI.getInfo().getTitle());
        assertEquals("v1", openAPI.getInfo().getVersion());
    }
}
