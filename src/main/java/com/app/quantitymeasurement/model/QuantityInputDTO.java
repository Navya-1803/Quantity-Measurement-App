package com.app.quantitymeasurement.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QuantityInputDTO is the request body of every POST endpoint. For
 * conversion, thatQuantityDTO gives the target unit. targetQuantityDTO is
 * optional and selects the result unit of add and subtract.
 *
 * @author Navya Raj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "First quantity cannot be null")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "Second quantity cannot be null")
    private QuantityDTO thatQuantityDTO;

    @Valid
    private QuantityDTO targetQuantityDTO;

    public QuantityInputDTO(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        this(thisQuantityDTO, thatQuantityDTO, null);
    }
}
