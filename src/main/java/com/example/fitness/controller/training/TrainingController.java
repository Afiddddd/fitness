package com.example.fitness.controller.training;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.service.training.TrainingService;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/training")
public class TrainingController {
    @Autowired private TrainingService trainingService;

    @PostMapping("/attempt")
    public ResponseEntity<?> attempt(@RequestParam int idSubscribe,
                                       @RequestParam int idTraining) throws ExceptionThrowable {
        return ResponseEntity.ok(trainingService.userAttemptTraining(idSubscribe, idTraining));
    }
}
