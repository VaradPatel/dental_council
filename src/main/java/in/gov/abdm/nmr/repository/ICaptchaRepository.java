package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.sql.Timestamp;

import in.gov.abdm.nmr.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICaptchaRepository extends JpaRepository<Captcha, BigInteger> {

    Captcha findByTransactionIdAndResultAndExpiredIsFalseAndExpiresAtAfter(String transId, Integer result, Timestamp now);

    Captcha findByTransactionIdAndExpiredIsTrueAndUpdatedAtBetween(String transId, Timestamp fiveMinutesBeforeNow, Timestamp now);
}
