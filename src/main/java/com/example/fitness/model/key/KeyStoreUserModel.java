package com.example.fitness.model.key;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Table(name = "tab_key_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeyStoreUserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "encryption_key")
    private byte[] encryptionKey;
    @Column(name = "decryption_key")
    private byte[] decryption_key;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
