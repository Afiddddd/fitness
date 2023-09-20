package com.example.fitness.model.fitness;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_subscriber_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "total_duration")
    private Integer totalDuration;
    @Column(name = "status")
    private boolean status;
    @Column(name = "fitness_type", columnDefinition = "ENUM('OVERWEIGHT','MAINTAIN')")
    private String fitnessType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
