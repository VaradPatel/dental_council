package in.gov.abdm.nmr.api.security.username_password;

import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -9000569283665023427L;

    private final Long userType;

    public UserPasswordAuthenticationToken(Object principal, Object credentials, Long userType) {
        super(principal, credentials);
        this.userType = userType;
    }

    public Long getUserType() {
        return userType;
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

    public static UserPasswordAuthenticationToken unauthenticated(Object principal, Object credentials, Long userType) {
        return new UserPasswordAuthenticationToken(principal, credentials, userType);
    }
}
