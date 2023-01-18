package in.gov.abdm.nmr.security.username_password;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPassDetail extends User {

    private static final long serialVersionUID = 3325160730962409090L;

    private final String userType;

    public UserPassDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, String userType,boolean accountLocked) {
        super(username, password, true,true,true,accountLocked,authorities);
        this.userType = userType;
    }

    public String getUserType() {
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
        UserPassDetail other = (UserPassDetail) obj;
        return Objects.equals(userType, other.userType);
    }
}
