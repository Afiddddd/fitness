package com.example.fitness.converter.auth;

import com.example.fitness.dto.auth.UserRegisterDTO;
import com.example.fitness.model.user.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserRegisterDTO convert(UserModel user){
        return new UserRegisterDTO(
                user.getIdUser(),
                user.getUserEmail()
        );
    }
}
