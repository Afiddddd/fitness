package com.example.fitness.security;

import com.example.fitness.dto.auth.UserLoginRequest;
import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.repository.user.UserRepository;
import com.example.fitness.service.auth.AuthService;
import com.example.fitness.service.auth.CustomUserDetailService;
import com.example.fitness.utils.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtService jwtService;

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private CustomUserDetailService userDetailService;
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            System.err.println("sampe sini doang");
            return;
        }
        jwt = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUserId(jwt);
        } catch (ExceptionThrowable e) {
            try {
                throw new ExceptionThrowable(HttpStatus.REQUEST_TIMEOUT.value(), "Link already expired");
            } catch (ExceptionThrowable ex) {
                try {
                    throw new ExceptionThrowable(ex.getErrorCode(),ex.getMessage());
                } catch (ExceptionThrowable exc) {
                    throw new RuntimeException(exc);
                }
            }
        }
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService
                    .loadUserByUsername(userEmail);
            try {
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            } catch (ExceptionThrowable e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }


}
