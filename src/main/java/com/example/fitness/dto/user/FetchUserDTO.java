package com.example.fitness.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchUserDTO {
    private long ccNumber;
    private Integer ccCvv;
    private String ccName;
}
