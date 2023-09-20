package com.example.fitness.controller.exception;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.response.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@ControllerAdvice
@RestController
public class ExceptionController {

//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<?> userNameNotFound(HttpServletResponse response, HttpServletRequest request,
//                                                     UsernameNotFoundException exc) throws Exception {
//        ResponseDTO dto = new ResponseDTO();
//            dto.setErrors(exc.getMessage());
//            dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return ResponseEntity.internalServerError().body(dto);
//    }
//
//    @ExceptionHandler(ServletException.class)
//    public ResponseEntity<?> userNameNotFound(HttpServletResponse response, HttpServletRequest request,
//                                              ServletException exc) throws Exception {
//        ResponseDTO dto = new ResponseDTO();
//        dto.setErrors(exc.getMessage());
//        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        return ResponseEntity.internalServerError().body(dto);
//    }

    @ExceptionHandler(ExceptionThrowable.class)
    public ResponseEntity<?> handlerRuntimeException(HttpServletResponse response, HttpServletRequest request,
                                                     ExceptionThrowable exc) throws IOException {
        ResponseDTO dto = new ResponseDTO();
        System.err.println("kesini kah?");
        if (exc.getErrorCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            dto.setErrors(exc.getMessage());
            dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return ResponseEntity.internalServerError().body(dto);
        } else if (exc.getErrorCode() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
            dto.setErrors(exc.getMessage());
            dto.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
            return ResponseEntity.unprocessableEntity().body(dto);
        } else if (exc.getErrorCode() == HttpStatus.REQUEST_TIMEOUT.value()) {
            System.err.println("tidak kesini");
            dto.setErrors(exc.getMessage());
            dto.setStatus(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
            return ResponseEntity.unprocessableEntity().body(dto);
        } else if (exc.getErrorCode() == HttpStatus.NOT_FOUND.value()) {
            dto.setErrors(exc.getMessage());
            dto.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
        return null;
    }

}


