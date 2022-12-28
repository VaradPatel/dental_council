package in.gov.abdm.nmr.api.security.jwt;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;

import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserSearchTO;

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
                jwtAuthtoken = new JwtAuthenticationToken(verifiedToken.getSubject(), Collections.emptyList());
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
                jwtAuthtoken = new JwtAuthenticationToken(verifiedToken.getSubject(), Collections.emptyList());
                return jwtAuthtoken;
            }
        } catch (Exception e) {
            LOGGER.error("Exception occured while authenticating JWT token", e);
        }
        throw new AuthenticationServiceException("Unable to authenticate JWT token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
