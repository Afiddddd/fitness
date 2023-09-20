package com.example.fitness.repository.user;

import com.example.fitness.model.user.CreditCardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCardModel, Integer> {
    Optional<CreditCardModel> findByUserId(String userId);
}
