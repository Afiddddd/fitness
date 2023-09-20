package com.example.fitness.repository.repository;

import com.example.fitness.model.key.KeyStoreUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeyStoreUserRepository extends JpaRepository<KeyStoreUserModel, Integer> {

    Optional<KeyStoreUserModel> findByUserId(String userId);
}
