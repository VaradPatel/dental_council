package in.gov.abdm.nmr.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.security.common.KeyUtil;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.IUserDaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Component
public class JwtUtil {

    public static final String AUTHORITIES_LABEL = "authorities";
    
    public static final String USER_PROFILE_ID_LABEL = "profile_id";

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String TOKEN_TYPE_LABEL = "type";

    private static final String ISSUER_VALUE = "nmc";

    private static final String AUDIENCE_VALUE = "nmr";

    private KeyUtil keyUtil;

    private Long accessTokenExpirySeconds;

    private Long refreshTokenExpirySeconds;

    private IUserDaoService userDetailService;

    public JwtUtil(KeyUtil keyUtil, @Value("${nmr.access.token.expiry}") Long accessTokenExpirySeconds, //
                   @Value("${nmr.refresh.token.expiry}") Long refreshTokenExpirySeconds, IUserDaoService userDetailService) {
        this.keyUtil = keyUtil;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.userDetailService = userDetailService;
    }

    public String generateToken(String username, JwtTypeEnum type, String role, BigInteger profileId) {
        LOGGER.info("Generating {}", type);

        if (RoleConstants.ROLE_SYSTEM.equals(role)) {
            accessTokenExpirySeconds = 3600l;
        }
        
        Long expiry = accessTokenExpirySeconds;
        String jwtId = UUID.randomUUID().toString();
        if (JwtTypeEnum.REFRESH_TOKEN.equals(type)) {
            expiry = refreshTokenExpirySeconds;

            UpdateRefreshTokenIdRequestTO updateRefreshTokenIdRequestTO = new UpdateRefreshTokenIdRequestTO();
            updateRefreshTokenIdRequestTO.setRefreshTokenId(jwtId);
            updateRefreshTokenIdRequestTO.setUsername(username);
            userDetailService.updateRefreshTokenId(updateRefreshTokenIdRequestTO);
        }

        Builder tokenBuilder = JWT.create().withJWTId(jwtId).withIssuer(ISSUER_VALUE).withSubject(username).withAudience(AUDIENCE_VALUE).withIssuedAt(Instant.now()) //
                .withExpiresAt(Instant.now().plusSeconds(expiry)).withClaim(TOKEN_TYPE_LABEL, type.getCode()).withClaim(AUTHORITIES_LABEL, Arrays.asList(role));
        if (profileId != null) {
            tokenBuilder.withClaim(USER_PROFILE_ID_LABEL, profileId.longValueExact());
        }

        return tokenBuilder.sign(Algorithm.RSA512(keyUtil.getPublicKey(), keyUtil.getPrivateKey()));
    }

    public DecodedJWT verifyToken(String token, JwtTypeEnum type) {
        LOGGER.info("Verifying {}", type);

        Verification verification = JWT.require(Algorithm.RSA512(keyUtil.getPublicKey(), keyUtil.getPrivateKey())).withIssuer(ISSUER_VALUE);
        return verification.withClaim(TOKEN_TYPE_LABEL, type.getCode()).build().verify(token);
    }

    public static boolean isTokenExpired(String token) {
        return JWT.decode(token).getExpiresAtAsInstant().isBefore(Instant.now());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
}
