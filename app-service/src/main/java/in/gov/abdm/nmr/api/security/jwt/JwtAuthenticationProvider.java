package in.gov.abdm.nmr.api.security.jwt;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private JwtUtil jwtUtil;

    public JwtAuthenticationProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        try {
            if (JwtUtil.TOKEN_ACCESS_CLAIM_VALUE.equals(token.getType())) {
                DecodedJWT verifiedToken = jwtUtil.verifyToken(token.getCredentials(), JwtUtil.TOKEN_ACCESS_CLAIM_VALUE);
                token = new JwtAuthenticationToken(verifiedToken.getSubject(), Collections.emptyList());
                return token;
            }

            if (JwtUtil.TOKEN_REFRESH_CLAIM_VALUE.equals(token.getType())) {
                DecodedJWT verifiedToken = jwtUtil.verifyToken(token.getCredentials(), JwtUtil.TOKEN_REFRESH_CLAIM_VALUE);
                token = new JwtAuthenticationToken(verifiedToken.getSubject(), Collections.emptyList());
                return token;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        throw new AuthenticationServiceException("Unable to authenticate token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
