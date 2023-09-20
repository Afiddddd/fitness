package com.example.fitness.controller.payment;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {
    @Autowired private PaymentService paymentService;

    @GetMapping("/total")
    public ResponseEntity<?> totalPrice(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam int idServices) throws ExceptionThrowable {
        return ResponseEntity.ok(paymentService.getTotalPrice(idServices));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam String userId,
                                     @RequestParam int idPaymentSubscriber) throws ExceptionThrowable {
        boolean b = paymentService.sendOtp(userId, idPaymentSubscriber);
        if (b) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("status","otp has been sent");
            return ResponseEntity.ok(data);
        }
           return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> validatePayment(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam String userId,
                                             @RequestParam int idPaymentSubscriber,
                                             @RequestParam int otp) throws ExceptionThrowable {
        boolean b = paymentService.validatePayment(userId, idPaymentSubscriber, otp);
        if (b) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("status","payment successful");
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
