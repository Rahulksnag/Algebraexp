package com.example.Algebraexp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import com.example.Algebraexp.model.Equation;
import com.example.Algebraexp.service.EquationService;
import com.example.Algebraexp.repository.EquationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EquationController.class)
public class EquationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquationService equationService;

    @MockBean
    private EquationRepository equationRepository;

    @BeforeEach
    public void setUp() {
        Equation equation = new Equation();
        equation.setId(1L);
        equation.setInfix("3x+2y-4z");
        equation.setPostfix("3 x * 2 y * + 4 z * -");

        Mockito.when(equationRepository.findById(1L)).thenReturn(java.util.Optional.of(equation));
        Mockito.when(equationService.evaluatePostfix(Mockito.anyString(), Mockito.anyMap())).thenReturn(1.0);
    }

    @Test
    public void testStoreEquation() throws Exception {
        Mockito.when(equationService.storeEquation(Mockito.anyString())).thenAnswer(invocation -> {
            Equation equation = new Equation();
            equation.setId(1L);
            equation.setInfix(invocation.getArgument(0));
            equation.setPostfix(equationService.infixToPostfix(invocation.getArgument(0)));
            return equation;
        });

        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"equation\": \"3x+2y-4z\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Equation stored successfully"))
                .andExpect(jsonPath("$.equationId").value(1));
    }

    @Test
    public void testGetAllEquations() throws Exception {
        mockMvc.perform(get("/api/equations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equations").isArray());
    }

    @Test
    public void testEvaluateEquation() throws Exception {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 1.0);
        variables.put("z", 1.0);

        mockMvc.perform(post("/api/equations/1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variables\":{\"x\":1,\"y\":1,\"z\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equationId").value(1))
                .andExpect(jsonPath("$.equation").value("3x+2y-4z"))
                .andExpect(jsonPath("$.variables.x").value(1))
                .andExpect(jsonPath("$.variables.y").value(1))
                .andExpect(jsonPath("$.variables.z").value(1))
                .andExpect(jsonPath("$.result").value(1.0));
    }
}