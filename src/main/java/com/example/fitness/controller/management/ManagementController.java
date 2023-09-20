package com.example.fitness.controller.management;

import com.example.fitness.dto.management.UserProfileRequest;
import com.example.fitness.service.management.ManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/management")
public class ManagementController {
    @Autowired private ManagementService managementService;

    @PutMapping("/edit/{user_id}")
    public ResponseEntity<?> editUser(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable(value = "user_id") String userId,
                                      @RequestBody UserProfileRequest dto) throws Exception {
       return ResponseEntity.ok(managementService.editUser(userId, dto));
    }
}
