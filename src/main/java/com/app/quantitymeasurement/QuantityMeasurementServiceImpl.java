package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementServiceImpl holds all the business logic for
 * comparison, conversion and arithmetic. For every operation it:
 * converts the DTOs to models, validates them, does the calculation,
 * saves a record in the repository and returns the result as a DTO.
 * Any failure is saved as an error record and rethrown as a
 * {@link QuantityMeasurementException}.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    private final IQuantityMeasurementRepository repository;

    // the repository is injected so that cache or database can be swapped easily
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl initialized with {}", repository.getClass().getSimpleName());
    }

    @Override
    public boolean compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        String operation = "compare";
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = getQuantityModel(thisQuantityDTO);
            thatQuantity = getQuantityModel(thatQuantityDTO);
            validateSameCategory(thisQuantity, thatQuantity, "compare");
            boolean result = Math.abs(toBase(thisQuantity) - toBase(thatQuantity)) < EPSILON;
            repository.save(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, String.valueOf(result)));
            return result;
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO targetUnitDTO) {
        String operation = "convert";
        QuantityModel<IMeasurable> thisQuantity = null;
        try {
            thisQuantity = getQuantityModel(thisQuantityDTO);
            IMeasurable targetUnit = resolveUnit(targetUnitDTO);
            validateSameCategory(thisQuantity, new QuantityModel<>(0.0, targetUnit), "convert");
            double converted = targetUnit.convertFromBaseUnit(toBase(thisQuantity));
            QuantityModel<IMeasurable> result = new QuantityModel<>(converted, targetUnit);
            repository.save(new QuantityMeasurementEntity(thisQuantity, operation, result));
            return toDTO(result);
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, null, e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic("add", ArithmeticOperation.ADD, thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic("subtract", ArithmeticOperation.SUBTRACT, thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    @Override
    public double divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        String operation = "divide";
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = getQuantityModel(thisQuantityDTO);
            thatQuantity = getQuantityModel(thatQuantityDTO);
            validateArithmetic(thisQuantity, thatQuantity, ArithmeticOperation.DIVIDE);
            double result = ArithmeticOperation.DIVIDE.compute(toBase(thisQuantity), toBase(thatQuantity));
            repository.save(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, result));
            return result;
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    // shared flow for add and subtract, the result is rounded to two decimals
    private QuantityDTO performArithmetic(String operation, ArithmeticOperation arithmeticOperation,
                                          QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        QuantityModel<IMeasurable> thisQuantity = null;
        QuantityModel<IMeasurable> thatQuantity = null;
        try {
            thisQuantity = getQuantityModel(thisQuantityDTO);
            thatQuantity = getQuantityModel(thatQuantityDTO);
            IMeasurable targetUnit = resolveUnit(targetUnitDTO);
            validateArithmetic(thisQuantity, thatQuantity, arithmeticOperation);
            validateSameCategory(thisQuantity, new QuantityModel<>(0.0, targetUnit), operation);
            double baseResult = arithmeticOperation.compute(toBase(thisQuantity), toBase(thatQuantity));
            double value = roundToTwoDecimals(targetUnit.convertFromBaseUnit(baseResult));
            QuantityModel<IMeasurable> result = new QuantityModel<>(value, targetUnit);
            repository.save(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, result));
            return toDTO(result);
        } catch (RuntimeException e) {
            throw handleError(operation, thisQuantity, thatQuantity, e);
        }
    }

    /**
     * Converts a DTO into a model with a real unit object.
     */
    QuantityModel<IMeasurable> getQuantityModel(QuantityDTO quantityDTO) {
        IMeasurable unit = resolveUnit(quantityDTO);
        if (!Double.isFinite(quantityDTO.getValue())) {
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

    private QuantityDTO toDTO(QuantityModel<IMeasurable> model) {
        return new QuantityDTO(model.getValue(), model.getUnit().getUnitName(), model.getUnit().getMeasurementType());
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // saves the failed operation and wraps the error into a QuantityMeasurementException
    private QuantityMeasurementException handleError(String operation, QuantityModel<IMeasurable> thisQuantity,
                                                     QuantityModel<IMeasurable> thatQuantity, RuntimeException e) {
        String message = operation + " Error: " + e.getMessage();
        logger.error(message);
        repository.save(new QuantityMeasurementEntity(thisQuantity, thatQuantity, operation, message, true));
        return new QuantityMeasurementException(message, e);
    }
}
