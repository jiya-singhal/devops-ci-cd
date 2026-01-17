package com.example.demo;

import org.springframework.stereotype.Service;

/**
 * Calculator Service
 * 
 * This service contains business logic for basic mathematical operations.
 * We include this to demonstrate:
 * 1. Unit testing (we'll write tests for these methods)
 * 2. Code quality checks (linting will verify this code)
 * 3. Security scanning (SAST will analyze this code)
 * 
 * @Service tells Spring this is a service component
 */
@Service
public class CalculatorService {

    /**
     * Adds two numbers together.
     * 
     * @param a First number
     * @param b Second number
     * @return Sum of a and b
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts second number from first.
     * 
     * @param a First number
     * @param b Second number to subtract
     * @return Difference (a - b)
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two numbers.
     * 
     * @param a First number
     * @param b Second number
     * @return Product of a and b
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides first number by second.
     * 
     * @param a Dividend
     * @param b Divisor
     * @return Quotient (a / b)
     * @throws IllegalArgumentException if b is zero
     */
    public double divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return (double) a / b;
    }
}
