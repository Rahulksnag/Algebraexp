package com.example.Algebraexp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Algebraexp.model.Equation;

@Repository
public interface EquationRepository extends JpaRepository<Equation, Long> {
    // Custom query methods can be defined here if needed
}