package com.app.quantitymeasurement;

import java.io.Serial;
import java.io.Serializable;

/**
 * QuantityMeasurementEntity stores everything about one operation: its
 * operands, the operation name and either the result or the error.
 * Fields are only set through the constructors, so an entity does not
 * change after it is created. Fields are not final because Java
 * serialization is used to save entities to disk.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private double thisValue;
    private String thisUnit;
    private String thisMeasurementType;
    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;
    private String operation;
    private String resultString;
    private double resultValue;
    private String resultUnit;
    private String resultMeasurementType;
    private String errorMessage;
    private boolean isError;

    // copies the first operand into the entity
    private void setThis(QuantityModel<?> thisQuantity) {
        this.thisValue = thisQuantity.getValue();
        this.thisUnit = thisQuantity.getUnit().getUnitName();
        this.thisMeasurementType = thisQuantity.getUnit().getMeasurementType();
    }

    // copies the second operand into the entity
    private void setThat(QuantityModel<?> thatQuantity) {
        this.thatValue = thatQuantity.getValue();
        this.thatUnit = thatQuantity.getUnit().getUnitName();
        this.thatMeasurementType = thatQuantity.getUnit().getMeasurementType();
    }

    // copies a quantity result into the entity
    private void setResult(QuantityModel<?> result) {
        this.resultValue = result.getValue();
        this.resultUnit = result.getUnit().getUnitName();
        this.resultMeasurementType = result.getUnit().getMeasurementType();
    }

    /**
     * Single operand operation with a quantity result, e.g. conversion.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, String operation, QuantityModel<?> result) {
        setThis(thisQuantity);
        this.operation = operation;
        setResult(result);
    }

    /**
     * Two operand operation with a text result, e.g. comparison.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, String resultString) {
        setThis(thisQuantity);
        setThat(thatQuantity);
        this.operation = operation;
        this.resultString = resultString;
    }

    /**
     * Two operand operation with a quantity result, e.g. addition or subtraction.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, QuantityModel<?> result) {
        setThis(thisQuantity);
        setThat(thatQuantity);
        this.operation = operation;
        setResult(result);
    }

    /**
     * Two operand operation with a plain number result, e.g. division.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, double resultValue) {
        setThis(thisQuantity);
        setThat(thatQuantity);
        this.operation = operation;
        this.resultValue = resultValue;
    }

    /**
     * Operation that failed, the second operand may be null.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, String errorMessage, boolean isError) {
        if (thisQuantity != null) {
            setThis(thisQuantity);
        }
        if (thatQuantity != null) {
            setThat(thatQuantity);
        }
        this.operation = operation;
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    public double getThisValue() {
        return thisValue;
    }

    public String getThisUnit() {
        return thisUnit;
    }

    public String getThisMeasurementType() {
        return thisMeasurementType;
    }

    public double getThatValue() {
        return thatValue;
    }

    public String getThatUnit() {
        return thatUnit;
    }

    public String getThatMeasurementType() {
        return thatMeasurementType;
    }

    public String getOperation() {
        return operation;
    }

    public String getResultString() {
        return resultString;
    }

    public double getResultValue() {
        return resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return isError;
    }

    @Override
    public String toString() {
        if (isError) {
            return "QuantityMeasurementEntity[operation=" + operation + ", error=" + errorMessage + "]";
        }
        String operands = "Quantity(" + thisValue + ", " + thisUnit + ")"
                + (thatUnit != null ? ", Quantity(" + thatValue + ", " + thatUnit + ")" : "");
        String result = resultString != null ? resultString
                : resultUnit != null ? "Quantity(" + resultValue + ", " + resultUnit + ")"
                : String.valueOf(resultValue);
        return "QuantityMeasurementEntity[operation=" + operation + ", operands=" + operands + ", result=" + result + "]";
    }
}
