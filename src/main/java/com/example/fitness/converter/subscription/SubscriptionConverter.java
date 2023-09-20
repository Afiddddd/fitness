package com.example.fitness.converter.subscription;

import com.example.fitness.dto.subscription.ListServicesFitnessDTO;
import com.example.fitness.dto.subscription.ListTrainingDTO;
import com.example.fitness.model.fitness.ListFitnessModel;
import com.example.fitness.model.fitness.ListTrainingModel;
import com.example.fitness.repository.fitness.ListTrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubscriptionConverter {

    @Autowired
    private ListTrainingRepository listTraining;

    public ListServicesFitnessDTO convertDTO(ListFitnessModel item) {
        List<ListTrainingDTO> dtos = new ArrayList<ListTrainingDTO>();

        listTraining.findByFitnessType(item.getFitnessType()).forEach(data -> {
            ListTrainingDTO dto = new ListTrainingDTO();
            dto.setId(data.getId());
            dto.setScheduleDate(data.getScheduleDate());
            dto.setTrainingName(data.getTrainingName());
            dto.setStartAt(data.getStartAt());
            dto.setEndAt(data.getEndAt());
            dtos.add(dto);
        });
        return new ListServicesFitnessDTO(
                item.getId(),
                item.getServiceName(),
                item.getPrice(),
                item.getTotalDuration(),
                item.getFitnessType(),
                dtos
        );
    }
}
