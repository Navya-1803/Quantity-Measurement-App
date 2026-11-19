package com.app.quantitymeasurement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * QuantityMeasurementEntity is the JPA entity stored for every operation:
 * its operands, the operation name and either the result or the error.
 *
 * @author Navya Raj
 */
@Entity
@Table(name = "quantity_measurement_entity", indexes = {
        @Index(name = "idx_operation", columnList = "operation"),
        @Index(name = "idx_measurement_type", columnList = "this_measurement_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value")
    private double thisValue;

    @Column(name = "this_unit", length = 50)
    private String thisUnit;

    @Column(name = "this_measurement_type", length = 50)
    private String thisMeasurementType;

    @Column(name = "that_value")
    private double thatValue;

    @Column(name = "that_unit", length = 50)
    private String thatUnit;

    @Column(name = "that_measurement_type", length = 50)
    private String thatMeasurementType;

    @Column(name = "operation", nullable = false, length = 20)
    private String operation;

    @Column(name = "result_string")
    private String resultString;

    @Column(name = "result_value")
    private double resultValue;

    @Column(name = "result_unit", length = 50)
    private String resultUnit;

    @Column(name = "result_measurement_type", length = 50)
    private String resultMeasurementType;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "is_error")
    private boolean isError;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Single operand operation with a quantity result, e.g. conversion.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, String operation, QuantityModel<?> result) {
        setThisQuantity(thisQuantity);
        this.operation = operation;
        setResultQuantity(result);
    }

    /**
     * Two operand operation with a text result, e.g. comparison.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, String resultString) {
        setThisQuantity(thisQuantity);
        setThatQuantity(thatQuantity);
        this.operation = operation;
        this.resultString = resultString;
    }

    /**
     * Two operand operation with a quantity result, e.g. addition or subtraction.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, QuantityModel<?> result) {
        setThisQuantity(thisQuantity);
        setThatQuantity(thatQuantity);
        this.operation = operation;
        setResultQuantity(result);
    }

    /**
     * Two operand operation with a plain number result, e.g. division.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, double resultValue) {
        setThisQuantity(thisQuantity);
        setThatQuantity(thatQuantity);
        this.operation = operation;
        this.resultValue = resultValue;
    }

    /**
     * Operation that failed, operands may be null when they could not be parsed.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity,
                                     String operation, String errorMessage, boolean isError) {
        if (thisQuantity != null) {
            setThisQuantity(thisQuantity);
        }
        if (thatQuantity != null) {
            setThatQuantity(thatQuantity);
        }
        this.operation = operation;
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    // copies the first operand into the entity
    private void setThisQuantity(QuantityModel<?> quantity) {
        this.thisValue = quantity.getValue();
        this.thisUnit = quantity.getUnit().getUnitName();
        this.thisMeasurementType = quantity.getUnit().getMeasurementType();
    }

    // copies the second operand into the entity
    private void setThatQuantity(QuantityModel<?> quantity) {
        this.thatValue = quantity.getValue();
        this.thatUnit = quantity.getUnit().getUnitName();
        this.thatMeasurementType = quantity.getUnit().getMeasurementType();
    }

    // copies a quantity result into the entity
    private void setResultQuantity(QuantityModel<?> quantity) {
        this.resultValue = quantity.getValue();
        this.resultUnit = quantity.getUnit().getUnitName();
        this.resultMeasurementType = quantity.getUnit().getMeasurementType();
    }

    // timestamps are filled automatically by JPA
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
