package com.example.fitness.model.fitness;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalIdCache;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_user_meet")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserMeetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "remaining_duration")
    private Integer remainingDuration;
    @Column(name = "total_duration")
    private Integer totalDuration;
    @Column(name = "id_subscribe")
    private Integer idSubscribe;
    @Column(name = "training_id")
    private Integer trainingId;
    @Column(name = "fitness_type", columnDefinition = "ENUM('OVERWEIGHT','MAINTAIN')")
    private String fitnessType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
