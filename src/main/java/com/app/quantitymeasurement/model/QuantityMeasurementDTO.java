package com.app.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * QuantityMeasurementDTO is the response sent back to REST clients for
 * every operation. It mirrors the entity but hides database details like
 * the id and timestamps.
 *
 * @author Navya Raj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementDTO {

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
    private boolean error;

    /**
     * Builds the response DTO from a stored entity.
     */
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        if (entity == null) {
            return null;
        }
        return QuantityMeasurementDTO.builder()
                .thisValue(entity.getThisValue())
                .thisUnit(entity.getThisUnit())
                .thisMeasurementType(entity.getThisMeasurementType())
                .thatValue(entity.getThatValue())
                .thatUnit(entity.getThatUnit())
                .thatMeasurementType(entity.getThatMeasurementType())
                .operation(entity.getOperation())
                .resultString(entity.getResultString())
                .resultValue(entity.getResultValue())
                .resultUnit(entity.getResultUnit())
                .resultMeasurementType(entity.getResultMeasurementType())
                .errorMessage(entity.getErrorMessage())
                .error(entity.isError())
                .build();
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream().map(QuantityMeasurementDTO::fromEntity).toList();
    }

    /**
     * Converts this DTO back to an entity, id and timestamps are left empty.
     */
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisValue);
        entity.setThisUnit(thisUnit);
        entity.setThisMeasurementType(thisMeasurementType);
        entity.setThatValue(thatValue);
        entity.setThatUnit(thatUnit);
        entity.setThatMeasurementType(thatMeasurementType);
        entity.setOperation(operation);
        entity.setResultString(resultString);
        entity.setResultValue(resultValue);
        entity.setResultUnit(resultUnit);
        entity.setResultMeasurementType(resultMeasurementType);
        entity.setErrorMessage(errorMessage);
        entity.setError(error);
        return entity;
    }
}
