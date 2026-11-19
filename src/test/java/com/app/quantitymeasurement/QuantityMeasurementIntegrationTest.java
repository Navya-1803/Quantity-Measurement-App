package com.app.quantitymeasurement;

import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuantityMeasurementIntegrationTest {

    private static final String BASE_URL = "/api/v1/quantities";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private static String body(double v1, String u1, String t1, double v2, String u2, String t2) {
        return "{\"thisQuantityDTO\":{\"value\":" + v1 + ",\"unit\":\"" + u1 + "\",\"measurementType\":\"" + t1 + "\"},"
                + "\"thatQuantityDTO\":{\"value\":" + v2 + ",\"unit\":\"" + u2 + "\",\"measurementType\":\"" + t2 + "\"}}";
    }

    @Test
    void testIntegrationTest_MultipleOperations() throws Exception {
        mockMvc.perform(post(BASE_URL + "/compare").contentType(MediaType.APPLICATION_JSON)
                        .content(body(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.resultString").value("true"));
        mockMvc.perform(post(BASE_URL + "/convert").contentType(MediaType.APPLICATION_JSON)
                        .content(body(100.0, "CELSIUS", "TemperatureUnit", 0.0, "FAHRENHEIT", "TemperatureUnit")))
                .andExpect(jsonPath("$.resultValue").value(212.0));
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON)
                        .content(body(1.0, "KILOGRAM", "WeightUnit", 1000.0, "GRAM", "WeightUnit")))
                .andExpect(jsonPath("$.resultValue").value(2.0)).andExpect(jsonPath("$.resultUnit").value("KILOGRAM"));
        mockMvc.perform(post(BASE_URL + "/subtract").contentType(MediaType.APPLICATION_JSON)
                        .content(body(5.0, "LITRE", "VolumeUnit", 500.0, "MILLILITRE", "VolumeUnit")))
                .andExpect(jsonPath("$.resultValue").value(4.5));
        mockMvc.perform(post(BASE_URL + "/divide").contentType(MediaType.APPLICATION_JSON)
                        .content(body(24.0, "INCHES", "LengthUnit", 2.0, "FEET", "LengthUnit")))
                .andExpect(jsonPath("$.resultValue").value(1.0));

        assertEquals(5, repository.count());
        mockMvc.perform(get(BASE_URL + "/count/COMPARE")).andExpect(content().string("1"));
        mockMvc.perform(get(BASE_URL + "/history/type/LengthUnit")).andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testErrorsAreStoredAndReported() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON)
                        .content(body(1.0, "FEET", "LengthUnit", 1.0, "KILOGRAM", "WeightUnit")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));
        mockMvc.perform(post(BASE_URL + "/divide").contentType(MediaType.APPLICATION_JSON)
                        .content(body(1.0, "FEET", "LengthUnit", 0.0, "INCHES", "LengthUnit")))
                .andExpect(status().isInternalServerError());
        mockMvc.perform(get(BASE_URL + "/history/errored")).andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testTemperatureArithmetic_Rejected() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON)
                        .content(body(100.0, "CELSIUS", "TemperatureUnit", 50.0, "CELSIUS", "TemperatureUnit")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidOperationInPath_Returns400() throws Exception {
        mockMvc.perform(get(BASE_URL + "/history/operation/MULTIPLY")).andExpect(status().isBadRequest());
    }
}
