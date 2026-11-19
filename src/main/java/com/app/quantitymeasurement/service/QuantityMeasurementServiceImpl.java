package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.ArithmeticOperation;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * QuantityMeasurementServiceImpl holds all the business logic for
 * comparison, conversion and arithmetic. For every operation it converts
 * the DTOs to models, validates them, does the calculation, stores a
 * record through Spring Data JPA and returns a QuantityMeasurementDTO.
 * Failures are stored as error records before being rethrown.
 *
 * @author Navya Raj
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    private final QuantityMeasurementRepository repository;

    // constructor injection keeps the dependency explicit and easy to mock
    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl initialized");
    }

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        String operation = OperationType.COMPARE.getDisplayName();
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = convertDtoToModel(thisQuantityDTO);
            thatQuantity = convertDtoToModel(thatQuantityDTO);
            validateSameCategory(thisQuantity, thatQuantity, operation);
            boolean result = Math.abs(toBase(thisQuantity) - toBase(thatQuantity)) < EPSILON;
            logger.debug("compare {} and {} -> {}", thisQuantity, thatQuantity, result);
            return saveAndConvert(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, String.valueOf(result)));
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO targetUnitDTO) {
        String operation = OperationType.CONVERT.getDisplayName();
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> targetQuantity = null;
        try {
            thisQuantity = convertDtoToModel(thisQuantityDTO);
            targetQuantity = convertDtoToModel(targetUnitDTO);
            validateSameCategory(thisQuantity, targetQuantity, operation);
            IMeasurable targetUnit = targetQuantity.getUnit();
            QuantityModel<IMeasurable> result = new QuantityModel<>(targetUnit.convertFromBaseUnit(toBase(thisQuantity)), targetUnit);
            logger.debug("convert {} -> {}", thisQuantity, result);
            return saveAndConvert(new QuantityMeasurementEntity(thisQuantity, targetQuantity, operation, result));
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, targetQuantity, e);
        }
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic(OperationType.ADD, ArithmeticOperation.ADD, thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic(OperationType.SUBTRACT, ArithmeticOperation.SUBTRACT, thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        String operation = OperationType.DIVIDE.getDisplayName();
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = convertDtoToModel(thisQuantityDTO);
            thatQuantity = convertDtoToModel(thatQuantityDTO);
            validateArithmetic(thisQuantity, thatQuantity, ArithmeticOperation.DIVIDE);
            double result = ArithmeticOperation.DIVIDE.compute(toBase(thisQuantity), toBase(thatQuantity));
            logger.debug("divide {} by {} -> {}", thisQuantity, thatQuantity, result);
            return saveAndConvert(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, result));
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        String storedName = OperationType.fromString(operation).getDisplayName();
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(storedName));
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public long getOperationCount(String operation) {
        String storedName = OperationType.fromString(operation).getDisplayName();
        return repository.countByOperationAndIsErrorFalse(storedName);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
    }

    // shared flow for add and subtract, the result is rounded to two decimals
    private QuantityMeasurementDTO performArithmetic(OperationType operationType, ArithmeticOperation arithmeticOperation,
                                                     QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO,
                                                     QuantityDTO targetUnitDTO) {
        String operation = operationType.getDisplayName();
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = convertDtoToModel(thisQuantityDTO);
            thatQuantity = convertDtoToModel(thatQuantityDTO);
            IMeasurable targetUnit = resolveUnit(targetUnitDTO == null ? thisQuantityDTO : targetUnitDTO);
            validateArithmetic(thisQuantity, thatQuantity, arithmeticOperation);
            validateSameCategory(thisQuantity, new QuantityModel<>(0.0, targetUnit), operation);
            double baseResult = arithmeticOperation.compute(toBase(thisQuantity), toBase(thatQuantity));
            double value = roundToTwoDecimals(targetUnit.convertFromBaseUnit(baseResult));
            QuantityModel<IMeasurable> result = new QuantityModel<>(value, targetUnit);
            logger.debug("{} {} and {} -> {}", operation, thisQuantity, thatQuantity, result);
            return saveAndConvert(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, result));
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    /**
     * Converts a DTO into a model with a real unit object.
     */
    QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO quantityDTO) {
        IMeasurable unit = resolveUnit(quantityDTO);
        if (quantityDTO.getValue() == null || !Double.isFinite(quantityDTO.getValue())) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return new QuantityModel<>(quantityDTO.getValue(), unit);
    }

    // finds the unit enum constant described by the DTO
    private IMeasurable resolveUnit(QuantityDTO quantityDTO) {
        if (quantityDTO == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        String measurementType = quantityDTO.getMeasurementType();
        if (measurementType == null) {
            throw new IllegalArgumentException("Measurement type cannot be null");
        }
        IMeasurable categoryUnit = switch (measurementType) {
            case "LengthUnit" -> LengthUnit.FEET;
            case "WeightUnit" -> WeightUnit.KILOGRAM;
            case "VolumeUnit" -> VolumeUnit.LITRE;
            case "TemperatureUnit" -> TemperatureUnit.CELSIUS;
            default -> throw new IllegalArgumentException("Unsupported measurement type: " + measurementType);
        };
        return categoryUnit.getUnitInstance(quantityDTO.getUnit());
    }

    // both quantities must belong to the same measurement category
    private void validateSameCategory(QuantityModel<IMeasurable> first, QuantityModel<IMeasurable> second, String operation) {
        String firstType = first.getUnit().getMeasurementType();
        String secondType = second.getUnit().getMeasurementType();
        if (!firstType.equals(secondType)) {
            throw new IllegalArgumentException("Cannot " + operation + " different measurement categories: "
                    + firstType + " and " + secondType);
        }
    }

    // checks category and operation support before any arithmetic
    private void validateArithmetic(QuantityModel<IMeasurable> first, QuantityModel<IMeasurable> second,
                                    ArithmeticOperation operation) {
        String firstType = first.getUnit().getMeasurementType();
        String secondType = second.getUnit().getMeasurementType();
        if (!firstType.equals(secondType)) {
            throw new IllegalArgumentException("Cannot perform arithmetic between different measurement categories: "
                    + firstType + " and " + secondType);
        }
        first.getUnit().validateOperationSupport(operation.name());
    }

    private double toBase(QuantityModel<IMeasurable> quantity) {
        return quantity.getUnit().convertToBaseUnit(quantity.getValue());
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private QuantityMeasurementDTO saveAndConvert(QuantityMeasurementEntity entity) {
        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    /**
     * Saves the failed operation, then returns the exception to throw.
     * Validation problems become a QuantityMeasurementException (client
     * error), other problems like division by zero are rethrown as they are.
     */
    private RuntimeException handleError(String operation, QuantityModel<IMeasurable> thisQuantity,
                                         QuantityModel<IMeasurable> thatQuantity, RuntimeException e) {
        logger.error("{} Error: {}", operation, e.getMessage());
        repository.save(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, e.getMessage(), true));
        if (e instanceof IllegalArgumentException || e instanceof UnsupportedOperationException) {
            return new QuantityMeasurementException(operation + " Error: " + e.getMessage(), e);
        }
        return e;
    }
}
