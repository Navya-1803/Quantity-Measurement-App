package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
class QuantityMeasurementControllerTest {

    private static final String BASE_URL = "/api/v1/quantities";

    private static final String VALID_BODY = """
            {"thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
             "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IQuantityMeasurementService service;

    private QuantityMeasurementDTO result(String operation, String resultString, double resultValue) {
        return QuantityMeasurementDTO.builder().thisValue(1.0).thisUnit("FEET").thisMeasurementType("LengthUnit")
                .operation(operation).resultString(resultString).resultValue(resultValue).build();
    }

    @Test
    void testRestEndpointCompareQuantities() throws Exception {
        when(service.compare(any(), any())).thenReturn(result("compare", "true", 0.0));
        mockMvc.perform(post(BASE_URL + "/compare").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testRestEndpointConvertQuantities() throws Exception {
        when(service.convert(any(), any())).thenReturn(result("convert", null, 12.0));
        mockMvc.perform(post(BASE_URL + "/convert").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    @Test
    void testRestEndpointAddQuantities() throws Exception {
        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(result("add", null, 2.0));
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0));
    }

    @Test
    void testRestEndpointAddWithTargetUnit() throws Exception {
        when(service.add(any(), any(), any())).thenReturn(result("add", null, 24.0));
        String body = """
                {"thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
                 "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"},
                 "targetQuantityDTO": {"value": 0.0, "unit": "INCHES", "measurementType": "LengthUnit"}}
                """;
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        verify(service).add(any(), any(), any());
    }

    @Test
    void testRestEndpointSubtractAndDivide() throws Exception {
        when(service.subtract(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(result("subtract", null, 0.0));
        when(service.divide(any(), any())).thenReturn(result("divide", null, 1.0));
        mockMvc.perform(post(BASE_URL + "/subtract").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk());
        mockMvc.perform(post(BASE_URL + "/divide").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(jsonPath("$.resultValue").value(1.0));
    }

    @Test
    void testRestEndpointInvalidInput_Returns400() throws Exception {
        String body = """
                {"thisQuantityDTO": {"value": 1.0, "unit": "FOOT", "measurementType": "LengthUnit"},
                 "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}}
                """;
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unit must be valid for the specified measurement type"));
        verifyNoInteractions(service);
    }

    @Test
    void testRestEndpointMissingParameter_Returns400() throws Exception {
        String body = """
                {"thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"}}
                """;
        mockMvc.perform(post(BASE_URL + "/compare").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testExceptionHandling_ServiceError_Returns400() throws Exception {
        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenThrow(new QuantityMeasurementException("add Error: Cannot perform arithmetic between different measurement categories"));
        mockMvc.perform(post(BASE_URL + "/add").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"))
                .andExpect(jsonPath("$.path").value(BASE_URL + "/add"));
    }

    @Test
    void testHttpStatusCodes_ServerErrors() throws Exception {
        when(service.divide(any(), any())).thenThrow(new ArithmeticException("Cannot divide by zero quantity"));
        mockMvc.perform(post(BASE_URL + "/divide").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Cannot divide by zero quantity"));
    }

    @Test
    void testRequestPathVariable_Extraction() throws Exception {
        when(service.getOperationHistory("COMPARE")).thenReturn(List.of(result("compare", "true", 0.0)));
        mockMvc.perform(get(BASE_URL + "/history/operation/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"));
        verify(service).getOperationHistory("COMPARE");
    }

    @Test
    void testGetMeasurementsByType() throws Exception {
        when(service.getMeasurementsByType("LengthUnit")).thenReturn(List.of(result("add", null, 2.0)));
        mockMvc.perform(get(BASE_URL + "/history/type/LengthUnit"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetOperationCount() throws Exception {
        when(service.getOperationCount("ADD")).thenReturn(4L);
        mockMvc.perform(get(BASE_URL + "/count/ADD")).andExpect(content().string("4"));
    }

    @Test
    void testGetErrorHistory() throws Exception {
        when(service.getErrorHistory()).thenReturn(List.of());
        mockMvc.perform(get(BASE_URL + "/history/errored")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void testContentNegotiation_XML() throws Exception {
        when(service.compare(any(), any())).thenReturn(result("compare", "true", 0.0));
        mockMvc.perform(post(BASE_URL + "/compare").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_XML).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("/QuantityMeasurementDTO/operation").string("compare"));
    }

    @Test
    void testMalformedJson_Returns400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/compare").contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest());
    }
}
