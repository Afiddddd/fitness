package com.example.fitness.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tab_credit_cards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "cc_name")
    private String ccName;
    @Column(name = "cc_number")
    private String ccNumber;
    @Column(name = "cvv")
    private String cvv;
    @Column(name = "cc_expiry")
    private String ccExpiry;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
