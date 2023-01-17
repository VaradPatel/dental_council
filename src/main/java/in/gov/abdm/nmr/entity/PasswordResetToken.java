package in.gov.abdm.nmr.entity;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Data;
import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class PasswordResetToken {

    private static final int EXPIRATION = NMRConstants.RESET_PASSWORD_LINK_EXPIRY_HOURS;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private String userName;
    private String token;
    private Timestamp expiryDate;

    public PasswordResetToken() {
        super();
    }

    public PasswordResetToken(final String token) {
        super();

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public PasswordResetToken(final String token, final String userName) {
        super();

        this.token = token;
        this.userName = userName;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    private Timestamp calculateExpiryDate(final int expiryTimeInMinutes) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, expiryTimeInMinutes);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp;
    }
}