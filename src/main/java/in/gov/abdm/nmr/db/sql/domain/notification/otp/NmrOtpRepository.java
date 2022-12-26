package in.gov.abdm.nmr.db.sql.domain.notification.otp;

import org.springframework.stereotype.Repository;

@Repository
public interface NmrOtpRepository {
    Otp saveOtpDetails(String otp, String email);

    Boolean doesValidDuplicateOtpExists(String otp, String email);

    Long findOtpGeneratedInLast15Minutes(String email);

    Long findOtpGeneratedInLast10MinutesWithExceededAttempts(String email);

}
