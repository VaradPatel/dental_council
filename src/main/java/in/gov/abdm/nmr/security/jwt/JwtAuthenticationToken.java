package in.gov.abdm.nmr.security.jwt;

import in.gov.abdm.nmr.enums.UserTypeEnum;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 4233846131436646907L;

    private final String principal;

    private final String token;

    private final JwtTypeEnum type;

    private final UserTypeEnum userType;

    public JwtAuthenticationToken(String token, JwtTypeEnum type, UserTypeEnum userType) {

        super(Collections.emptyList());
        this.principal = null;
        this.token = token;
        this.type = type;
        this.userType = userType;
    }

    public JwtAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities, UserTypeEnum userType) {
        super(authorities);
        this.principal = principal;
        this.token = null;
        this.type = null;
        this.userType = userType;
    }


    @Override
    public String getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public JwtTypeEnum getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(principal, token, type);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        JwtAuthenticationToken other = (JwtAuthenticationToken) obj;
        return Objects.equals(principal, other.principal) && Objects.equals(token, other.token) && type == other.type;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }
}
