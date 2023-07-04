package in.gov.abdm.nmr.security.username_password;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPassDetail extends User {

    private static final long serialVersionUID = 3325160730962409090L;

    private final BigInteger userType;
    
    private final String mobileNumber;

    public UserPassDetail(String username, String mobileNumber, String password,
                          Collection<? extends GrantedAuthority> authorities, BigInteger userType, boolean accountNonLocked) {
        super(username, password, true, true, true, accountNonLocked, authorities);
        this.userType = userType;
        this.mobileNumber = mobileNumber;
    }

    public BigInteger getUserType() {
        return userType;
    }

    public String getMobileNumber() {
        return mobileNumber;
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
        UserPassDetail other = (UserPassDetail) obj;
        return Objects.equals(userType, other.userType);
    }
}
