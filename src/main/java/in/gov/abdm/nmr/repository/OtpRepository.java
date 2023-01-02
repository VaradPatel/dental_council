package in.gov.abdm.nmr.repository;

import java.util.List;

import in.gov.abdm.nmr.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, String> {
    List<Otp> findByExpiredIsFalseAndContactIs(String contact);

    Otp findOneByExpiredIsFalseAndContactIs(String contact);

    List<Otp> findByExpiredIsFalseAndOtpHashIsAndContactNot(String otpHash, String contact);

    Otp findOneByExpiredIsFalseAndContactIsAndOtpHashIs(String contact, String otp);

    Otp findOneByIdIsAndContactIs(String transactionId, String contact);

}
