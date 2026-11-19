package com.app.quantitymeasurement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title and description shown on the Swagger UI page.
 *
 * @author Navya Raj
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI quantityMeasurementOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Quantity Measurement API")
                .description("Compare, convert, add, subtract and divide length, weight, volume and temperature quantities")
                .version("v1")
                .contact(new Contact().name("Navya Raj")));
    }
}
