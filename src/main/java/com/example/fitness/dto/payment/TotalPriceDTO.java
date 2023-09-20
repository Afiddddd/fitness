package com.example.fitness.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalPriceDTO {
    private int idPayment;
    private String servicesName;
    private double totalPrice;
    private Integer totalDuration;
    private String fitnessType;
}
