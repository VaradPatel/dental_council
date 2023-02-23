package in.gov.abdm.nmr.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.service.IUserDaoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private JwtUtil jwtUtil;

    private IUserDaoService userDetailService;

    public JwtAuthenticationProvider(JwtUtil jwtUtil, IUserDaoService userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthtoken = (JwtAuthenticationToken) authentication;
        try {
            if (JwtTypeEnum.ACCESS_TOKEN.equals(jwtAuthtoken.getType())) {
                DecodedJWT verifiedToken = jwtUtil.verifyToken(jwtAuthtoken.getCredentials(), JwtTypeEnum.ACCESS_TOKEN);
                List<? extends GrantedAuthority> authorities = verifiedToken.getClaim(JwtUtil.AUTHORITIES_LABEL).asList(String.class).stream().map(SimpleGrantedAuthority::new).toList();
                jwtAuthtoken = new JwtAuthenticationToken(verifiedToken.getSubject(), authorities);
                jwtAuthtoken.setAuthenticated(true);
                return jwtAuthtoken;
            }

            if (JwtTypeEnum.REFRESH_TOKEN.equals(jwtAuthtoken.getType())) {
                DecodedJWT verifiedToken = jwtUtil.verifyToken(jwtAuthtoken.getCredentials(), JwtTypeEnum.REFRESH_TOKEN);

                UserSearchTO userDetailSearchTO = new UserSearchTO();
                userDetailSearchTO.setUsername(verifiedToken.getSubject());
                String refreshTokenId = userDetailService.findRefreshTokenId(userDetailSearchTO);

                if (StringUtils.isBlank(verifiedToken.getId()) || StringUtils.isBlank(refreshTokenId) || !refreshTokenId.equals(verifiedToken.getId())) {
                    throw new AuthenticationServiceException("Unable to authenticate token");
                }
                List<? extends GrantedAuthority> authorities = verifiedToken.getClaim(JwtUtil.AUTHORITIES_LABEL).asList(String.class).stream().map(SimpleGrantedAuthority::new).toList();
                jwtAuthtoken = new JwtAuthenticationToken(verifiedToken.getSubject(), authorities);
                jwtAuthtoken.setAuthenticated(true);
                return jwtAuthtoken;
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while authenticating JWT token", e);

            if (e instanceof TokenExpiredException) {
                throw new InvalidBearerTokenException("Token expired");
            }

            if (e instanceof JWTVerificationException) {
                throw new InvalidBearerTokenException("Invalid token");
            }
            throw new AuthenticationServiceException("Exception occurred while authenticating JWT token");
        }
        throw new AuthenticationServiceException("Unable to authenticate JWT token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
