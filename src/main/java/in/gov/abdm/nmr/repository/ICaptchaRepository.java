package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface ICaptchaRepository extends JpaRepository<Captcha, BigInteger> {

    Captcha findByTransactionIdAndResultAndExpiredIsFalseAndExpiresAtAfter(String transId, Integer result, Timestamp now);

    Captcha findByTransactionIdAndExpiredIsTrueAndUpdatedAtBetween(String transId, Timestamp fiveMinutesBeforeNow, Timestamp now);
}
