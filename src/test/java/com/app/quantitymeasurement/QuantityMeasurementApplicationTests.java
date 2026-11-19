package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuantityMeasurementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    // verifies that the spring application context loads without errors
    @Test
    void testSpringBootApplicationStarts() {
    }

    @Test
    void testOpenAPIDocumentation() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Quantity Measurement API"))
                .andExpect(jsonPath("$.paths['/api/v1/quantities/compare']").exists());
    }

    @Test
    void testSwaggerUILoads() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

    @Test
    void testActuatorHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testActuatorMetricsEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/metrics")).andExpect(status().isOk()).andExpect(jsonPath("$.names").isArray());
    }
}
