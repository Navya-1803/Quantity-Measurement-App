package com.app.quantitymeasurement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * QuantityDTO carries one quantity (value, unit name and measurement type)
 * between the client, the controller and the service. Validation
 * annotations make sure only well formed quantities reach the service.
 *
 * @author Navya Raj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

    /**
     * Unit contract used only by this DTO so that callers can build a DTO
     * without depending on the service layer units.
     */
    public interface IMeasurableUnit {

        String getUnitName();

        String getMeasurementType();
    }

    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCHES, YARDS, CENTIMETERS;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "LengthUnit";
        }
    }

    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, POUND;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "WeightUnit";
        }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        LITRE, MILLILITRE, GALLON;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "VolumeUnit";
        }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "TemperatureUnit";
        }
    }

    @NotNull(message = "Value cannot be null")
    private Double value;

    @NotEmpty(message = "Unit cannot be empty")
    private String unit;

    @NotEmpty(message = "Measurement type cannot be empty")
    @Pattern(regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
            message = "Measurement type must be one of LengthUnit, WeightUnit, VolumeUnit or TemperatureUnit")
    private String measurementType;

    public QuantityDTO(double value, IMeasurableUnit unit) {
        this(value, unit.getUnitName(), unit.getMeasurementType());
    }

    /**
     * Cross field check: the unit must belong to the given measurement type,
     * e.g. FEET is valid for LengthUnit but not for WeightUnit.
     */
    @JsonIgnore
    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isValidUnit() {
        if (unit == null || measurementType == null) {
            // the @NotEmpty checks already report this case
            return true;
        }
        IMeasurableUnit[] units = switch (measurementType) {
            case "LengthUnit" -> LengthUnit.values();
            case "WeightUnit" -> WeightUnit.values();
            case "VolumeUnit" -> VolumeUnit.values();
            case "TemperatureUnit" -> TemperatureUnit.values();
            default -> new IMeasurableUnit[0];
        };
        return Arrays.stream(units).anyMatch(u -> u.getUnitName().equalsIgnoreCase(unit));
    }
}
