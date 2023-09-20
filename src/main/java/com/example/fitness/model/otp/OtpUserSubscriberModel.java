package com.example.fitness.model.otp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_subscriber_otp")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OtpUserSubscriberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "id_payment_subscriber")
    private int idPaymentSubscriber;
    @Column(name = "id_user")
    private String idUser;
    @Column(name = "otp")
    private Integer otp;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

}

