package in.gov.abdm.nmr.security.username_password;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigInteger;
import java.util.Objects;

public class UserPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -9000569283665023427L;

    private final BigInteger userType;
    
    private final BigInteger loginType;
    
    private final String otpTransactionId;

    public UserPasswordAuthenticationToken(Object principal, Object credentials, BigInteger userType, BigInteger loginType, String otpTransactionId) {
        super(principal, credentials);
        this.userType = userType;
        this.loginType = loginType;
        this.otpTransactionId = otpTransactionId;
    }

    public BigInteger getUserType() {
        return userType;
    }

    public BigInteger getLoginType() {
        return loginType;
    }

    public String getOtpTransactionId() {
        return otpTransactionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(userType);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        UserPasswordAuthenticationToken other = (UserPasswordAuthenticationToken) obj;
        return Objects.equals(userType, other.userType);
    }

    public static UserPasswordAuthenticationToken unauthenticated(Object principal, Object credentials, BigInteger userType, BigInteger loginType, String otpTransactionId) {
        return new UserPasswordAuthenticationToken(principal, credentials, userType, loginType, otpTransactionId);
    }
}
