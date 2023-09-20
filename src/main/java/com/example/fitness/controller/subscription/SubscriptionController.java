package com.example.fitness.controller.subscription;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.service.subscription.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/services")
    public ResponseEntity<?> listServices(HttpServletResponse response, HttpServletRequest request) {
        return ResponseEntity.ok(subscriptionService.listServices());
    }
    @GetMapping("/users")
    public ResponseEntity<?> statusSubscriber(HttpServletResponse response, HttpServletRequest request,
                                              @RequestParam String userId) throws ExceptionThrowable {
        return ResponseEntity.ok(subscriptionService.statusSubscriber(userId));
    }
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelServices(HttpServletResponse response, HttpServletRequest request,
                                              @RequestParam int subscriberUser) throws ExceptionThrowable {
        return ResponseEntity.ok(subscriptionService.cancelServices(subscriberUser));
    }
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam int listService,
                                       @RequestParam String userId,
                                       @RequestParam int totalDuration) throws ExceptionThrowable {
        return ResponseEntity.ok(subscriptionService.subscribe(listService, userId, totalDuration));
    }
}
