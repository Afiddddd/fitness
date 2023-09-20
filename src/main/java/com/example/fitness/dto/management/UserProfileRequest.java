package com.example.fitness.dto.management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    private String firstName = null;
    private String lastName = null;
    private String password;
    private long ccNumber = 0;
    private String ccName = null;
    private Integer ccCvv = null;
    private LocalDate ccExpires = null;
}
