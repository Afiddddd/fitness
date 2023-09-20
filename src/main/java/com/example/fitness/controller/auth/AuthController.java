package com.example.fitness.controller.auth;

import com.example.fitness.dto.auth.RegistrationRequest;
import com.example.fitness.dto.auth.UserLoginRequest;
import com.example.fitness.dto.auth.UserRegisterDTO;
import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.response.ResponseDTO;
import com.example.fitness.dto.user.FetchUserDTO;
import com.example.fitness.service.auth.AuthService;
import com.example.fitness.service.auth.CustomUserDetailService;
import com.example.fitness.service.auth.JwtAuthenticationResponse;
import com.example.fitness.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = {"application/json"})
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomUserDetailService userDetailService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest data) throws ExceptionThrowable {
            UserRegisterDTO register = authService.register(data.getEmail(), data.getPassword(),
                    data.getCcNumber(),data.getCcName(), data.getCcCvv(),data.getCcExpires());

            return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserLoginRequest request) throws ExceptionThrowable {
        System.err.println(request.getEmail());
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Ambil informasi autentikasi saat ini
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lakukan operasi logout di sini, misalnya menghapus token dari daftar token yang valid
        // Anda juga bisa melakukan operasi lain sesuai kebutuhan aplikasi Anda

        // Setelah logout, hapus informasi autentikasi dari konteks SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(null);

        // Anda juga bisa mengirimkan respons yang sesuai, misalnya berhasil logout atau pesan lainnya
        return ResponseEntity.ok("Logout berhasil");
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetch(@RequestParam String userId) {
        try {
            FetchUserDTO creditCardModel = authService.getCreditCardModel(userId);
            System.err.println(creditCardModel.getCcNumber());
            return ResponseEntity.ok(creditCardModel);
        }catch (RuntimeException e){
            ResponseDTO dto = new ResponseDTO();
            dto.setErrors(e.getMessage());
            dto.setStatus("unprocessable_entity");
            return ResponseEntity.unprocessableEntity().body(dto);
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(HttpServletRequest req, HttpServletResponse res,
                                      @RequestParam String token) throws ExceptionThrowable {

            return ResponseEntity.ok(authService.activateUserRegistration(token));

    }
}
