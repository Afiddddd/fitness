package com.example.fitness.repository.fitness;

import com.example.fitness.model.fitness.ListFitnessModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListFitnessRepository extends JpaRepository<ListFitnessModel, Integer> {
}
