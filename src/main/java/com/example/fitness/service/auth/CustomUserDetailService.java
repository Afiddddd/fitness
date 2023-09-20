package com.example.fitness.service.auth;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.model.user.UserModel;
import com.example.fitness.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmailValidator emailValidator = EmailValidator.getInstance();


        if (!emailValidator.isValid(email)){;
            throw new UsernameNotFoundException("Email is not valid");
        }
        UserModel user = userRepository.findByUserEmail(email).orElseThrow(() -> new UsernameNotFoundException("404"));
        System.err.println("apakah dsni");

        return new User(user.getUserEmail(), user.getUserPassword(), new ArrayList<>());
    }
}
