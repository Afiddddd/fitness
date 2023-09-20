package com.example.fitness.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListServicesFitnessDTO {
    private int id;
    private String serviceName;
    private double price;
    private int totalDuration;
    private String fitnessType;
    private List<ListTrainingDTO> listTraining;
}
