package in.gov.abdm.nmr.entity;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class ResetToken {

    private static final int EXPIRATION = NMRConstants.RESET_PASSWORD_LINK_EXPIRY_HOURS;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private String userName;
    private BigInteger userType;
    private String token;
    private Timestamp expiryDate;

    public ResetToken() {
        super();
    }

    public ResetToken(final String token) {
        super();

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public ResetToken(final String token, final String userName, BigInteger userType) {
        super();

        this.token = token;
        this.userName = userName;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.userType=userType;
    }
    private Timestamp calculateExpiryDate(final int expiryTimeInHours) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, expiryTimeInHours);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public void updateExpiryTime(){
        this.setExpiryDate(calculateExpiryDate(EXPIRATION));
    }
}