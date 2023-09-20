package com.example.fitness.service.subscription;

import com.example.fitness.converter.subscription.SubscriptionConverter;
import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.payment.TotalPriceDTO;
import com.example.fitness.dto.subscription.ListServicesFitnessDTO;
import com.example.fitness.model.fitness.ListFitnessModel;
import com.example.fitness.model.fitness.UserSubscriberModel;
import com.example.fitness.repository.fitness.ListFitnessRepository;
import com.example.fitness.repository.fitness.UserSubscriberRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    @Autowired
    private ListFitnessRepository listFitnessRepository;
    @Autowired
    private SubscriptionConverter subscriptionConverter;
    @Autowired private UserSubscriberRepository userSubscriberRepository;
    public List<ListServicesFitnessDTO> listServices() {
        return listFitnessRepository.findAll().stream().map(subscriptionConverter::convertDTO).collect(Collectors.toList());
    }

    public UserSubscriberModel statusSubscriber(String userId) throws ExceptionThrowable {
        return userSubscriberRepository.findByUserId(userId).orElseThrow(() -> new ExceptionThrowable(404, "User not found"));
    }

    public UserSubscriberModel cancelServices(int subscriberUser) throws ExceptionThrowable {
        UserSubscriberModel data = userSubscriberRepository.findById(subscriberUser).orElseThrow(() -> new ExceptionThrowable(404, "Item not found"));
        data.setStatus(false);
        return userSubscriberRepository.save(data);
    }

    public TotalPriceDTO subscribe(int listService, String userId, int totalDuration) throws ExceptionThrowable {
        try {
            ListFitnessModel dataFitness = listFitnessRepository.findById(listService).orElseThrow(() -> new ExceptionThrowable(404, "Fitness service not found"));
            double price = dataFitness.getPrice() * totalDuration;
            UserSubscriberModel model = new UserSubscriberModel();
            model.setStatus(false);
            model.setCreatedAt(LocalDateTime.now());
            model.setUserId(userId);
            model.setTotalDuration(totalDuration);
            model.setFitnessType(dataFitness.getFitnessType());
            int id = userSubscriberRepository.save(model).getId();
            return new TotalPriceDTO(
                    id,
                    dataFitness.getServiceName(),
                    price,
                    totalDuration,
                    dataFitness.getFitnessType()
            );
        }catch (ExceptionThrowable e){
            throw new ExceptionThrowable(e.getErrorCode(),e.getMessage());
        }

    }
}
