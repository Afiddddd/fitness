package com.example.fitness.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Validator {

    public boolean isValidEmail(String email) {
        // Validasi email menggunakan ekspresi reguler atau pustaka validasi email
        // Contoh menggunakan ekspresi reguler seperti yang sudah dijelaskan sebelumnya
        String emailPattern = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
        return email.matches(emailPattern);
    }
    public boolean isStrongPassword(String password) {
        // Validasi kekuatan kata sandi
        // Contoh: Kata sandi harus memiliki panjang minimal 8 karakter,
        // minimal satu huruf besar, satu huruf kecil, satu angka, dan satu karakter khusus
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }

    public int generateRandomFiveDigitNumber() {
        Random random = new Random();
        int min = 10000; // Minimum 5-digit number
        int max = 99999; // Maximum 5-digit number

        // Generate a random number between min and max (inclusive)

        return random.nextInt((max - min) + 1) + min;
    }
}
