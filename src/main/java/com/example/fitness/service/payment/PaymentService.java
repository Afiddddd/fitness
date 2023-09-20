package com.example.fitness.service.payment;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.dto.payment.TotalPriceDTO;
import com.example.fitness.model.fitness.ListFitnessModel;
import com.example.fitness.model.fitness.UserSubscriberModel;
import com.example.fitness.model.otp.OtpUserSubscriberModel;
import com.example.fitness.model.user.UserModel;
import com.example.fitness.repository.fitness.ListFitnessRepository;
import com.example.fitness.repository.fitness.UserSubscriberRepository;
import com.example.fitness.repository.otp.OtpUserSubscriberRepository;
import com.example.fitness.repository.user.UserRepository;
import com.example.fitness.utils.EmailManager;
import com.example.fitness.utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired private ListFitnessRepository listFitnessRepository;
    @Autowired private EmailManager emailManager;
    @Autowired private UserRepository userRepository;
    @Autowired private Validator validator;
    @Autowired private OtpUserSubscriberRepository otpUserSubscriberRepository;
    @Autowired private UserSubscriberRepository userSubscriberRepository;
    public TotalPriceDTO getTotalPrice(int idServices) throws ExceptionThrowable {
        TotalPriceDTO dto = new TotalPriceDTO();
        ListFitnessModel data = listFitnessRepository.findById(idServices).orElseThrow(() -> new ExceptionThrowable(404, "Not Found"));

        double totalPrice = data.getPrice() * data.getTotalDuration();

        dto.setServicesName(data.getServiceName());
        dto.setFitnessType(data.getFitnessType());
        dto.setTotalDuration(data.getTotalDuration());
        dto.setTotalPrice(totalPrice);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sendOtp(String userId, int idPaymentSubscriber) throws ExceptionThrowable {
        UserModel userModel = userRepository.findByIdUser(userId).orElseThrow(() -> new ExceptionThrowable(404, "User not found"));
        try {
            int otp = validator.generateRandomFiveDigitNumber();
            String subject = "Payment OTP";
            String message = "Kode OTP Anda adalah: " + otp;

            emailManager.sendEmail(userModel.getUserEmail(), subject, message);
            OtpUserSubscriberModel model = new OtpUserSubscriberModel();
            model.setOtp(otp);
            model.setCreatedAt(LocalDateTime.now());
            model.setExpiredAt(LocalDateTime.now().plusMinutes(5));
            model.setIdUser(userId);
            model.setIdPaymentSubscriber(idPaymentSubscriber);
            otpUserSubscriberRepository.save(model);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new ExceptionThrowable(500, "Email send failed");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean validatePayment(String userId, int idPaymentSubscriber, int otp) throws ExceptionThrowable {
        List<OtpUserSubscriberModel> otpUser = otpUserSubscriberRepository.getOtpUser(idPaymentSubscriber, userId, otp, LocalDateTime.now());
        if (!otpUser.isEmpty()){
            UserSubscriberModel userSubscriberModel = userSubscriberRepository.findById(idPaymentSubscriber).get();
            userSubscriberModel.setStatus(true);
            userSubscriberRepository.save(userSubscriberModel);
            return true;
        }
        throw new ExceptionThrowable(HttpStatus.REQUEST_TIMEOUT.value(), "Otp has expired");
    }
}
