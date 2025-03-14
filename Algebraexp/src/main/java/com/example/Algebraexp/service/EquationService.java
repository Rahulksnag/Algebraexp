package com.example.Algebraexp.service;

import java.util.Map;
import java.util.Stack;
import org.springframework.stereotype.Service;
import com.example.Algebraexp.model.Equation;
import com.example.Algebraexp.repository.EquationRepository;

@Service
public class EquationService {
    private final EquationRepository equationRepository;

    public EquationService(EquationRepository equationRepository) {
        this.equationRepository = equationRepository;
    }

    public Equation storeEquation(String infix) {
        String postfix = infixToPostfix(infix);
        Equation equation = new Equation();
        equation.setInfix(infix);
        equation.setPostfix(postfix);
        return equationRepository.save(equation);
    }

    public String infixToPostfix(String infix) {
        Stack<String> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        StringBuilder operand = new StringBuilder();
        char previousChar = ' ';

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            // Skip spaces
            if (c == ' ') {
                continue;
            }

            if (Character.isLetterOrDigit(c) || c == '.') {
                // Check for implicit multiplication
                if (i > 0 && (Character.isDigit(previousChar) && Character.isLetter(c) || Character.isLetter(previousChar) && Character.isLetter(c))) {
                    postfix.append(operand).append(' ');
                    operand.setLength(0);
                    while (!stack.isEmpty() && precedence('*') <= precedence(stack.peek().charAt(0))) {
                        postfix.append(stack.pop()).append(' ');
                    }
                    stack.push("*");
                }
                operand.append(c);
            } else {
                if (operand.length() > 0) {
                    postfix.append(operand).append(' ');
                    operand.setLength(0);
                }
                if (c == '(') {
                    stack.push(String.valueOf(c));
                } else if (c == ')') {
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        postfix.append(stack.pop()).append(' ');
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek().charAt(0))) {
                        postfix.append(stack.pop()).append(' ');
                    }
                    stack.push(String.valueOf(c));
                }
            }
            previousChar = c;
        }

        if (operand.length() > 0) {
            postfix.append(operand).append(' ');
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString().trim();
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    public double evaluatePostfix(String postfix, Map<String, Double> variables) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix.split(" ")) {
            if (variables.containsKey(token)) {
                stack.push(variables.get(token));
            } else if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression: insufficient operands for operator " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        stack.push(a / b); break;
                    case "^": stack.push(Math.pow(a, b)); break;
                    default: throw new IllegalArgumentException("Invalid operator: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression: remaining operands after evaluation");
        }

        return stack.pop();
    }
}