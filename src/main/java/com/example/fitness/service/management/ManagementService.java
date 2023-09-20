package com.example.fitness.service.management;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.management.UserProfileRequest;
import com.example.fitness.model.key.KeyStoreUserModel;
import com.example.fitness.model.user.CreditCardModel;
import com.example.fitness.model.user.UserModel;
import com.example.fitness.repository.repository.KeyStoreUserRepository;
import com.example.fitness.repository.user.CreditCardRepository;
import com.example.fitness.repository.user.UserRepository;
import com.example.fitness.utils.KeyManager;
import com.example.fitness.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ManagementService {
    @Autowired private UserRepository userRepository;
    @Autowired private KeyStoreUserRepository keyStoreUserRepository;
    @Autowired private KeyManager keyManager;
    @Autowired private CreditCardRepository creditCardRepository;
    @Autowired private Validator validator;
    @Autowired private PasswordEncoder passwordEncoder;

    public boolean editUser(String userId, UserProfileRequest dto) throws Exception {
        UserModel data = userRepository.findByIdUser(userId).orElseThrow(() -> new ExceptionThrowable(404, "User not found"));
        KeyStoreUserModel keyStoreUser = keyStoreUserRepository.findByUserId(userId).orElseThrow(() -> new ExceptionThrowable(500, "Internal server error"));
        CreditCardModel creditCardModel = creditCardRepository.findByUserId(userId).orElseThrow(() -> new ExceptionThrowable(404, "Credit card not found"));
        try {
            if (dto.getFirstName() != null) {
                data.setFistName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                data.setLastName(dto.getLastName());
            }
            if (dto.getPassword() != null) {
                if (!validator.isStrongPassword(dto.getPassword())){
                    throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Password tidak valid.");
                }
                data.setUserPassword(passwordEncoder.encode(dto.getPassword()));
            }
            data.setUserPassword(data.getUserPassword());
            data.setFistName(data.getFistName());
            data.setLastName(data.getLastName());

            SecretKey originalKey = new SecretKeySpec(keyStoreUser.getEncryptionKey(), 0, keyStoreUser.getEncryptionKey().length, "AES");
            if (dto.getCcCvv() != null) {
                creditCardModel.setCvv(keyManager.encrypt(String.valueOf(dto.getCcCvv()), originalKey));

            }
            if (dto.getCcNumber() != 0) {
                if (String.valueOf(dto.getCcNumber()).length() != 16){
                    throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"nomer kartu tidak valid.");
                }
                creditCardModel.setCcNumber(keyManager.encrypt(String.valueOf(dto.getCcNumber()), originalKey));
            }
            if (dto.getCcName() != null) {
                creditCardModel.setCcName(keyManager.encrypt(dto.getCcName(), originalKey));
            }
            if (dto.getCcExpires() != null) {
                creditCardModel.setCcExpiry(keyManager.encrypt(String.valueOf(dto.getCcExpires()), originalKey));
            }
            creditCardRepository.save(creditCardModel);
            userRepository.save(data);
            return true;

        }catch (ExceptionThrowable e) {
            throw new ExceptionThrowable(e.getErrorCode(),e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Edit user failed");
        }
    }
}
