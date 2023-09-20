package com.example.fitness.repository.fitness;

import com.example.fitness.model.fitness.UserSubscriberModel;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSubscriberRepository extends JpaRepository<UserSubscriberModel,Integer> {
    Optional<UserSubscriberModel> findByUserId(String userId);


}
