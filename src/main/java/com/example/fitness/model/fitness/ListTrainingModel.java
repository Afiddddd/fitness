package com.example.fitness.model.fitness;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tab_list_training")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListTrainingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "training_name")
    private String trainingName;
    @Column(name = "training_schedule")
    private String scheduleDate;
    @Column(name = "start_at")
    private LocalTime startAt;
    @Column(name = "end_at")
    private LocalTime endAt;
    @Column(name = "fitness_type", columnDefinition = "ENUM('OVERWEIGHT','MAINTAIN')")
    private String fitnessType;
}
