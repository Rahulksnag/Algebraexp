package com.example.Algebraexp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EquationServiceTest {

    private EquationService equationService;

    @BeforeEach
    public void setUp() {
        equationService = new EquationService(null); // Pass null for repository since it's not used in these tests
    }

    @Test
    public void testInfixToPostfix() {
        String infix = "3x+2y-4z";
        String expectedPostfix = "3 x * 2 y * + 4 z * -";
        String actualPostfix = equationService.infixToPostfix(infix);
        assertEquals(expectedPostfix, actualPostfix);
    }

    @Test
    public void testEvaluatePostfix() {
        String postfix = "3 x * 2 y * + 4 z * -";
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 1.0);
        variables.put("z", 1.0);

        double expected = 1.0;
        double actual = equationService.evaluatePostfix(postfix, variables);
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluatePostfixWithInvalidExpression() {
        String postfix = "3 x * +";
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluatePostfix(postfix, variables);
        });
    }
}