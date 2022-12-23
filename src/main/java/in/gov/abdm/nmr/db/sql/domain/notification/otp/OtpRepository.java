package in.gov.abdm.nmr.db.sql.domain.notification.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, String> {
    List<Otp> findByExpiredIsFalseAndContactIs(String contact);

    Otp findOneByExpiredIsFalseAndContactIs(String contact);

    List<Otp> findByExpiredIsFalseAndOtpHashIsAndContactNot(String otpHash, String contact);

    Otp findOneByExpiredIsFalseAndContactIsAndOtpHashIs(String contact, String otp);

    Otp findOneByIdIsAndContactIs(String transactionId, String contact);

}

