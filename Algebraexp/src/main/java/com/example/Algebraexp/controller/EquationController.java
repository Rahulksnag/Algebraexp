package com.example.Algebraexp.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Algebraexp.model.Equation;
import com.example.Algebraexp.service.EquationService;
import com.example.Algebraexp.repository.EquationRepository;

@RestController
@RequestMapping("/api/equations")
public class EquationController {
    private final EquationService equationService;
    private final EquationRepository equationRepository;

    public EquationController(EquationService equationService, EquationRepository equationRepository) {
        this.equationService = equationService;
        this.equationRepository = equationRepository;
    }

    @PostMapping("/store")
    public ResponseEntity<?> storeEquation(@RequestBody Map<String, String> request) {
        String equation = request.get("equation");
        Equation storedEquation = equationService.storeEquation(equation);
        return ResponseEntity.ok(Map.of("message", "Equation stored successfully", "equationId", storedEquation.getId()));
    }

    @GetMapping
    public ResponseEntity<?> getAllEquations() {
        List<Equation> equations = equationRepository.findAll();
        List<Map<String, Object>> formattedEquations = equations.stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("equationId", e.getId());
                    map.put("equation", e.getInfix());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("equations", formattedEquations));
    }

    @GetMapping("/{equationId}")
    public ResponseEntity<?> getEquation(@PathVariable Long equationId) {
        Equation equation = equationRepository.findById(equationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equation ID"));
        Map<String, Object> response = new HashMap<>();
        response.put("equationId", equation.getId());
        response.put("equation", equation.getInfix());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<?> evaluateEquation(@PathVariable Long equationId, @RequestBody Map<String, Map<String, Double>> variablesWrapper) {
        Map<String, Double> variables = variablesWrapper.get("variables");
        Equation equation = equationRepository.findById(equationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equation ID"));
        double result = equationService.evaluatePostfix(equation.getPostfix(), variables);
        Map<String, Object> response = new HashMap<>();
        response.put("equationId", equationId);
        response.put("equation", equation.getInfix());
        response.put("variables", variables);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }
}