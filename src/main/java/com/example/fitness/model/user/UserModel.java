package com.example.fitness.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "id_user")
    private String idUser;
    @Column(name = "first_name")
    private String fistName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String userEmail;
    @Column(name = "password")
    private String userPassword;
    @Column(name = "password_show")
    private String userPasswordShow;
    @Column(name = "user_status", columnDefinition = "ENUM()")
    private String userStatus;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
