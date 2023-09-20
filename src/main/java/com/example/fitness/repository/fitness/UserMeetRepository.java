package com.example.fitness.repository.fitness;

import com.example.fitness.model.fitness.UserMeetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMeetRepository extends JpaRepository<UserMeetModel,Integer> {

    @Query(value = "SELECT * FROM tab_user_meet tum " +
            "where tum.id_subscribe = :idSubscribe " +
            "order by tum.created_at DESC LIMIT 1 ", nativeQuery = true)
    Optional<UserMeetModel> getTrainingData(int idSubscribe);
}
