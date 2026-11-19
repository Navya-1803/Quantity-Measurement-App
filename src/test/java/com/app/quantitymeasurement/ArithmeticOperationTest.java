package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticOperationTest {

    private static final double DELTA = 1e-9;

    @Test
    void testArithmeticOperation_Add_EnumComputation() {
        assertEquals(15.0, ArithmeticOperation.ADD.compute(10.0, 5.0), DELTA);
    }

    @Test
    void testArithmeticOperation_Subtract_EnumComputation() {
        assertEquals(5.0, ArithmeticOperation.SUBTRACT.compute(10.0, 5.0), DELTA);
    }

    @Test
    void testArithmeticOperation_Divide_EnumComputation() {
        assertEquals(2.0, ArithmeticOperation.DIVIDE.compute(10.0, 5.0), DELTA);
    }

    @Test
    void testArithmeticOperation_DivideByZero_EnumThrows() {
        assertThrows(ArithmeticException.class, () -> ArithmeticOperation.DIVIDE.compute(10.0, 0.0));
    }

    @Test
    void testEnumConstant_ADD_CorrectlyAdds() {
        assertEquals(-1.5, ArithmeticOperation.ADD.compute(-3.0, 1.5), DELTA);
    }

    @Test
    void testEnumConstant_SUBTRACT_CorrectlySubtracts() {
        assertEquals(-4.5, ArithmeticOperation.SUBTRACT.compute(-3.0, 1.5), DELTA);
    }

    @Test
    void testEnumConstant_DIVIDE_CorrectlyDivides() {
        assertEquals(-2.0, ArithmeticOperation.DIVIDE.compute(-3.0, 1.5), DELTA);
    }

    @Test
    void testEnumDispatch_AllOperations_CorrectlyDispatched() {
        assertEquals(3, ArithmeticOperation.values().length);
        assertEquals(ArithmeticOperation.ADD, ArithmeticOperation.valueOf("ADD"));
    }
}
