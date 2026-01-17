package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Calculator Service Tests
 * 
 * These are UNIT TESTS - they verify that individual pieces of code work correctly.
 * 
 * WHY UNIT TESTS MATTER IN CI/CD:
 * 1. They catch bugs BEFORE code is deployed
 * 2. They run automatically on every code push
 * 3. They prevent regressions (breaking things that used to work)
 * 4. They serve as documentation of expected behavior
 * 
 * The CI pipeline will run these tests automatically and FAIL the build
 * if any test fails. This prevents broken code from being deployed.
 */
class CalculatorServiceTest {

    private CalculatorService calculator;

    /**
     * This method runs BEFORE each test.
     * It creates a fresh CalculatorService instance for each test.
     */
    @BeforeEach
    void setUp() {
        calculator = new CalculatorService();
    }

    // ADDITION TESTS

    @Test
    @DisplayName("Addition: 2 + 3 should equal 5")
    void testAddPositiveNumbers() {
        // Arrange: Set up test data
        int a = 2;
        int b = 3;
        
        // Act: Perform the operation
        int result = calculator.add(a, b);
        
        // Assert: Verify the result
        assertEquals(5, result, "2 + 3 should equal 5");
    }

    @Test
    @DisplayName("Addition: handles negative numbers")
    void testAddNegativeNumbers() {
        assertEquals(-5, calculator.add(-2, -3), "Adding two negatives");
        assertEquals(1, calculator.add(-2, 3), "Adding negative and positive");
    }

    @Test
    @DisplayName("Addition: adding zero returns same number")
    void testAddZero() {
        assertEquals(5, calculator.add(5, 0), "Adding zero should return same number");
    }

    // SUBTRACTION TESTS

    @Test
    @DisplayName("Subtraction: 5 - 3 should equal 2")
    void testSubtractPositiveNumbers() {
        assertEquals(2, calculator.subtract(5, 3));
    }

    @Test
    @DisplayName("Subtraction: handles negative results")
    void testSubtractResultingInNegative() {
        assertEquals(-2, calculator.subtract(3, 5));
    }

    // MULTIPLICATION TESTS 

    @Test
    @DisplayName("Multiplication: 4 * 3 should equal 12")
    void testMultiply() {
        assertEquals(12, calculator.multiply(4, 3));
    }

    @Test
    @DisplayName("Multiplication: anything times zero equals zero")
    void testMultiplyByZero() {
        assertEquals(0, calculator.multiply(5, 0));
        assertEquals(0, calculator.multiply(0, 5));
    }

    @Test
    @DisplayName("Multiplication: negative numbers")
    void testMultiplyNegatives() {
        assertEquals(-12, calculator.multiply(4, -3), "Positive times negative");
        assertEquals(12, calculator.multiply(-4, -3), "Negative times negative");
    }

    //  DIVISION TESTS 

    @Test
    @DisplayName("Division: 10 / 2 should equal 5")
    void testDivide() {
        assertEquals(5.0, calculator.divide(10, 2));
    }

    @Test
    @DisplayName("Division: handles decimal results")
    void testDivideWithDecimalResult() {
        assertEquals(2.5, calculator.divide(5, 2));
    }

    @Test
    @DisplayName("Division: throws exception when dividing by zero")
    void testDivideByZero() {
        // This test verifies that our code properly handles errors
        // Dividing by zero should throw an IllegalArgumentException
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.divide(10, 0)
        );
        
        assertEquals("Cannot divide by zero", exception.getMessage());
    }
}
