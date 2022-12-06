package in.gov.abdm.nmr.api.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.api.security.common.ProtectedPaths;

@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new OrRequestMatcher(ProtectedPaths.getProtectedPathsMatchers()), authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            if (StringUtils.isNotBlank(request.getHeader(HttpHeaders.AUTHORIZATION))) {
                String accessToken = extractBearerToken(request);

                JwtTypeEnum tokenType = JwtTypeEnum.ACCESS_TOKEN;
                if (ProtectedPaths.PATH_REFRESH_TOKEN.equals(request.getServletPath())) {
                    tokenType = JwtTypeEnum.REFRESH_TOKEN;
                }

                JwtAuthenticationToken authRequest = new JwtAuthenticationToken(accessToken, tokenType);
                authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } catch (Exception e) {
            LOGGER.error("Exception occured while parsing bearer token", e);
        }
        throw new AuthenticationServiceException("Unable to parse bearer");
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            return authHeader.substring(authHeader.indexOf(" ") + 1, authHeader.length());
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) //
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
