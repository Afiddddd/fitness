package com.example.fitness.repository.user;

import com.example.fitness.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByUserEmail(String email);
    Optional<UserModel> findByIdUser(String userId);
}
