package com.app.quantitymeasurement.unit;

/**
 * VolumeUnit is the enum of supported volume units. Litre is the base unit.
 * Only this enum is needed to support volume, the generic Quantity class
 * handles everything else.
 *
 * @author Navya Raj
 */
public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    // 1 mL = 0.001 L
    MILLILITRE(0.001),
    // 1 US gallon = 3.78541 L
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }
        for (VolumeUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(unitName.trim())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid unit '" + unitName + "' for VolumeUnit");
    }
}
