package in.gov.abdm.nmr.api.security.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

import in.gov.abdm.nmr.api.security.common.KeyUtil;

@Component
public class JwtUtil {

    private static final String TOKEN_TYPE = "token_type";

    private static final String KEY_ALIAS = "jwt";

    private static final String ISSUER = "nmc";

    private static final String AUDIENCE = "nmr";

    public static final String TOKEN_ACCESS_CLAIM_VALUE = "at";

    public static final String TOKEN_REFRESH_CLAIM_VALUE = "rt";

    private KeyUtil keyUtil;

    private Long accessTokenExpirySeconds;

    private Long refreshTokenExpirySeconds;

    public JwtUtil(KeyUtil keystoreService, @Value("${nmr.access.token.expiry}") Long accessTokenExpirySeconds, @Value("${nmr.refresh.token.expiry}") Long refreshTokenExpirySeconds) {
        this.keyUtil = keystoreService;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public String generateToken(String username, String type) {
        Long expiry = TOKEN_ACCESS_CLAIM_VALUE.equals(type) ? accessTokenExpirySeconds : refreshTokenExpirySeconds;
        return JWT.create().withIssuer(ISSUER).withSubject(username).withAudience(AUDIENCE).withIssuedAt(Instant.now()).withExpiresAt(Instant.now() //
                .plusSeconds(expiry)).withClaim(TOKEN_TYPE, type).sign(Algorithm.RSA256(keyUtil.getPublicKey(KEY_ALIAS), keyUtil.getPrivateKey(KEY_ALIAS)));
    }

    public DecodedJWT verifyToken(String token, String type) {
        Verification verification = JWT.require(Algorithm.RSA256(keyUtil.getPublicKey(KEY_ALIAS), keyUtil.getPrivateKey(KEY_ALIAS))).withIssuer(ISSUER);
        return verification.withClaim(TOKEN_TYPE, type).build().verify(token);
    }

    public static boolean isTokenExpired(String token) {
        return JWT.decode(token).getExpiresAtAsInstant().isBefore(Instant.now());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
}
