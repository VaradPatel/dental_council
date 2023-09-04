package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.security.username_password.UserPasswordAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigInteger;

public class TestUsernamePasswordAuthenticationToken extends UserPasswordAuthenticationToken {

    public TestUsernamePasswordAuthenticationToken(Object principal, Object credentials, BigInteger userType, BigInteger loginType, String otpTransactionId) {
        super(principal, credentials, userType, loginType, otpTransactionId);
    }
}
