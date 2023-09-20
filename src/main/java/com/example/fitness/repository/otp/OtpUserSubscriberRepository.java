package com.example.fitness.repository.otp;

import com.example.fitness.model.otp.OtpUserSubscriberModel;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OtpUserSubscriberRepository extends JpaRepository<OtpUserSubscriberModel,Integer> {
    @Query(value = "select * from tab_subscriber_otp as tso " +
            "where tso.id_payment_subscriber = :idPaymentSubscriber " +
            "and tso.id_user = :idUser " +
            "and tso.otp = :otp " +
            "and tso.expired_at > :date ", nativeQuery = true)
    List<OtpUserSubscriberModel> getOtpUser(int idPaymentSubscriber, String idUser, int otp, LocalDateTime date);
}
