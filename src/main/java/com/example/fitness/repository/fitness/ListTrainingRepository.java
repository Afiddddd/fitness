package com.example.fitness.repository.fitness;

import com.example.fitness.model.fitness.ListTrainingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListTrainingRepository extends JpaRepository<ListTrainingModel,Integer> {
    List<ListTrainingModel> findByFitnessType(String fitnessType);
}
