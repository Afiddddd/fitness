package com.example.fitness.service.training;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.model.fitness.UserMeetModel;
import com.example.fitness.model.fitness.UserSubscriberModel;
import com.example.fitness.repository.fitness.UserMeetRepository;
import com.example.fitness.repository.fitness.UserSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TrainingService {

    @Autowired private UserMeetRepository userMeetRepository;
    @Autowired private UserSubscriberRepository userSubscriberRepository;

    public Object userAttemptTraining(int idSubscribe, int trainingId) throws ExceptionThrowable {
        try {
            UserMeetModel model = new UserMeetModel();
            UserMeetModel data = userMeetRepository.getTrainingData(idSubscribe).orElseGet(UserMeetModel::new);
            UserSubscriberModel dataSubscriber = userSubscriberRepository.findById(idSubscribe).orElseThrow(() -> new ExceptionThrowable(404, "Subscriber not found"));
            if (data.getTrainingId() == null) {
                model.setRemainingDuration(dataSubscriber.getTotalDuration() - 1);
            }else{
                model.setRemainingDuration(data.getRemainingDuration() - 1);
            }
            model.setTrainingId(trainingId);
            model.setUserId(dataSubscriber.getUserId());
            model.setCreatedAt(LocalDateTime.now());
            model.setTotalDuration(dataSubscriber.getTotalDuration());
            model.setFitnessType(dataSubscriber.getFitnessType());
            model.setIdSubscribe(idSubscribe);
            return userMeetRepository.save(model);

        } catch (ExceptionThrowable e) {
            throw new ExceptionThrowable(e.getErrorCode(),e.getMessage());
        }
    }
}
