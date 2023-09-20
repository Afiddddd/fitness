package com.example.fitness.service.auth;

import com.example.fitness.converter.auth.UserConverter;
import com.example.fitness.dto.auth.UserLoginRequest;
import com.example.fitness.dto.auth.UserRegisterDTO;
import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.user.FetchUserDTO;
import com.example.fitness.model.key.KeyStoreUserModel;
import com.example.fitness.model.user.CreditCardModel;
import com.example.fitness.model.user.UserModel;
import com.example.fitness.repository.repository.KeyStoreUserRepository;
import com.example.fitness.repository.user.CreditCardRepository;
import com.example.fitness.repository.user.UserRepository;
import com.example.fitness.utils.EmailManager;
import com.example.fitness.utils.KeyManager;
import com.example.fitness.utils.JwtService;
import com.example.fitness.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthService {


    @Autowired private Validator validator;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserConverter userConverter;
    @Autowired private KeyManager keyManager;
    @Autowired private CreditCardRepository creditCardRepository;
    @Autowired private KeyStoreUserRepository keyStoreUserRepository;
    @Autowired private EmailManager emailManager;
    @Autowired private JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserRegisterDTO register(String email, String password, long ccNumber,
                                    String ccName, Integer ccCvv, LocalDate ccExpire) throws ExceptionThrowable {
        EmailValidator emailValidator = EmailValidator.getInstance();
            if (!emailValidator.isValid(email)){
                throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Email tidak valid.");
            }

            if (!validator.isStrongPassword(password)){
                throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Password tidak valid.");
            }

            if(userRepository.findByUserEmail(email).isPresent()){
                throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Email sudah terdaftar.");
            }
            if (String.valueOf(ccNumber).length() != 16){
                throw new ExceptionThrowable(HttpStatus.UNPROCESSABLE_ENTITY.value(),"nomer kartu tidak valid.");
            }
            try {

            UserModel user = new UserModel();
            user.setUserEmail(email);
            user.setUserPassword(passwordEncoder.encode(password));
            user.setUserPasswordShow(password);
            user.setIdUser(UUID.randomUUID().toString());
            user.setCreatedAt(LocalDateTime.now());

            saveCreditCardUser(ccNumber, ccName, ccCvv, ccExpire, user.getIdUser());
            // Send OTP via email
            String subject = "Aktivasi";
            String message = "Kode OTP Anda adalah: " + jwtService.generateToken(user.getIdUser());

            emailManager.sendEmail(email, subject, message);

            return userConverter.convert(userRepository.save(user));
        }catch (Exception e) {
            throw new ExceptionThrowable(500, e.getMessage());
        }

    }

    public void saveCreditCardUser(long ccNumber, String ccName, Integer ccCvv,
                                   LocalDate ccExpiry, String userId) throws ExceptionThrowable {
        try {
            // Generate kunci enkripsi baru
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // Atur panjang kunci sesuai kebutuhan
            SecretKey encryptionKey = keyGen.generateKey();


            keyManager.saveKeysUser(encryptionKey,userId);

            // Enkripsi data kartu kredit
            String encryptedCardNumber = keyManager.encrypt(String.valueOf(ccNumber), encryptionKey);
            String encryptedExpiryDate = keyManager.encrypt(String.valueOf(ccExpiry), encryptionKey);
            String encryptedCvv = keyManager.encrypt(String.valueOf(ccCvv), encryptionKey);
            String encryptName = keyManager.encrypt(ccName, encryptionKey);
            CreditCardModel model = new CreditCardModel();
            model.setUserId(userId);
            model.setCreatedAt(LocalDateTime.now());
            model.setCvv(encryptedCvv);
            model.setCcName(encryptName);
            model.setCcExpiry(encryptedExpiryDate);
            model.setCcNumber(encryptedCardNumber);
            creditCardRepository.save(model);
        }catch (Exception e){
            e.printStackTrace();
            throw new ExceptionThrowable(500, e.getMessage());
        }

    }

    public FetchUserDTO getCreditCardModel(String userId)throws RuntimeException{
        try {
            System.err.println(userId);
            KeyStoreUserModel keyStore = keyStoreUserRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("userId not found"));
            CreditCardModel creditCardModel = creditCardRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("user Id not found"));
            // Menghasilkan kunci dekripsi dari bytes kunci enkripsi

            Key decryptionKey = new SecretKeySpec(keyStore.getEncryptionKey(), "AES");
            String ccNumber = keyManager.decrypt(creditCardModel.getCcNumber(), decryptionKey);
            String ccName = keyManager.decrypt(creditCardModel.getCcName(), decryptionKey);
            String ccCvv = keyManager.decrypt(creditCardModel.getCvv(), decryptionKey);
            String ccExpiry = keyManager.decrypt(creditCardModel.getCcExpiry(), decryptionKey);

            logger.info(ccNumber);


            return new FetchUserDTO(
                    Long.parseLong(ccNumber),
                    Integer.parseInt(ccCvv),
                    ccName
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return new FetchUserDTO();
    }

    public boolean activateUserRegistration(String token) throws ExceptionThrowable {

            if (jwtService.isTokenExpired(token)){
                throw new ExceptionThrowable(HttpStatus.REQUEST_TIMEOUT.value(), "Link already expired");
            }
        try {
            String userId = jwtService.extractUserId(token);
            UserModel userRegistration = userRepository.findByIdUser(userId).orElseThrow(() -> new IllegalStateException("User registration"));
            userRegistration.setUserStatus("Y");
            userRepository.save(userRegistration);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public JwtAuthenticationResponse signIn(UserLoginRequest request) throws ExceptionThrowable {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        System.err.println("kalau dsni masuk tidak");
        var user = userRepository.findByUserEmail(request.getEmail());
        if (user.isEmpty()){
            throw new ExceptionThrowable(HttpStatus.NOT_FOUND.value(), "User not found");
        }
        String login = null;
        var jwt = jwtService.generateTokenLogin(login,user.get().getUserEmail());
        return new JwtAuthenticationResponse(jwt);
    }
}
