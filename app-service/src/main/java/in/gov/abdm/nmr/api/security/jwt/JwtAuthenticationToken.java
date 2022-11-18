package in.gov.abdm.nmr.api.security.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 4233846131436646907L;

    private final String principal;

    private final String token;

    private final String type;

    public JwtAuthenticationToken(String token, String type) {
        super(Collections.emptyList());
        this.principal = "";
        this.token = token;
        this.type = type;
    }

    public JwtAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = "";
        this.type = "";
    }


    @Override
    public String getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getType() {
        return type;
    }
}
