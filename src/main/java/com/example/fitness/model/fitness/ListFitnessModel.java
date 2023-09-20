package com.example.fitness.model.fitness;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_list_fitness")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListFitnessModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "price")
    private double price;
    @Column(name = "total_duration")
    private Integer totalDuration;
    @Column(name = "fitness_type", columnDefinition = "ENUM('OVERWEIGHT','MAINTAIN')")
    private String fitnessType;
}
