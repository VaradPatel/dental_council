package in.gov.abdm.nmr.api.security.jwt;

import java.time.Instant;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

import in.gov.abdm.nmr.api.db.user_detail.IUserDetailService;
import in.gov.abdm.nmr.api.security.common.KeyUtil;

@Component
public class JwtUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String TOKEN_TYPE_LABEL = "type";

    private static final String KEY_ALIAS = "jwt";

    private static final String ISSUER_VALUE = "nmc";

    private static final String AUDIENCE_VALUE = "nmr";

    private KeyUtil keyUtil;

    private String privateKeyPass;

    private Long accessTokenExpirySeconds;

    private Long refreshTokenExpirySeconds;

    private IUserDetailService userDetailService;

    public JwtUtil(KeyUtil keyUtil, @Value("${nmr.jwt.private.key.pass}") String privateKeyPass, @Value("${nmr.access.token.expiry}") Long accessTokenExpirySeconds, //
                   @Value("${nmr.refresh.token.expiry}") Long refreshTokenExpirySeconds, IUserDetailService userDetailService) {
        this.keyUtil = keyUtil;
        this.privateKeyPass = privateKeyPass;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.userDetailService = userDetailService;
    }

    public String generateToken(String username, JwtTypeEnum type) {
        LOGGER.info("Generating {}", type);

        Long expiry = accessTokenExpirySeconds;
        String jwtId = UUID.randomUUID().toString();
        if (JwtTypeEnum.REFRESH_TOKEN.equals(type)) {
            expiry = refreshTokenExpirySeconds;
            userDetailService.updateRefreshTokenId(username, jwtId);
        }

        return JWT.create().withJWTId(jwtId).withIssuer(ISSUER_VALUE).withSubject(username).withAudience(AUDIENCE_VALUE).withIssuedAt(Instant.now()) //
                .withExpiresAt(Instant.now().plusSeconds(expiry)).withClaim(TOKEN_TYPE_LABEL, type.getCode()) //
                .sign(Algorithm.RSA512(keyUtil.getPublicKey(KEY_ALIAS), keyUtil.getPrivateKey(KEY_ALIAS, privateKeyPass)));
    }

    public DecodedJWT verifyToken(String token, JwtTypeEnum type) {
        LOGGER.info("Verifying {}", type);

        Verification verification = JWT.require(Algorithm.RSA512(keyUtil.getPublicKey(KEY_ALIAS), keyUtil.getPrivateKey(KEY_ALIAS, privateKeyPass))).withIssuer(ISSUER_VALUE);
        return verification.withClaim(TOKEN_TYPE_LABEL, type.getCode()).build().verify(token);
    }

    public static boolean isTokenExpired(String token) {
        return JWT.decode(token).getExpiresAtAsInstant().isBefore(Instant.now());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
}
