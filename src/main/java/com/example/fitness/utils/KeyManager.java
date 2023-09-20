package com.example.fitness.utils;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.model.key.KeyStoreUserModel;
import com.example.fitness.repository.repository.KeyStoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@Component
public class KeyManager {
    @Autowired
    private KeyStoreUserRepository keyStoreUserRepository;
    public String encrypt(String dataToEncrypt, Key encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        byte[] encryptedData = cipher.doFinal(dataToEncrypt.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData, Key decryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        System.err.println(Arrays.toString(decryptedData));
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public void saveKeysUser(SecretKey encryptionKey,String userId) throws ExceptionThrowable {
        try {
            KeyStoreUserModel model = new KeyStoreUserModel();
            model.setEncryptionKey(encryptionKey.getEncoded());
            model.setUserId(userId);
            model.setCreatedAt(LocalDateTime.now());
            keyStoreUserRepository.save(model);

        }catch (Exception e){
            throw new ExceptionThrowable(500,e.getMessage());
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Panjang kunci yang disarankan
        return keyPairGenerator.generateKeyPair();
    }

}
