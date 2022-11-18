package in.gov.abdm.nmr.api.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.api.security.common.ProtectedPaths;
import in.gov.abdm.nmr.api.security.controller.AuthController;

@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new OrRequestMatcher(ProtectedPaths.getProtectedPathsMatchers()), authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (StringUtils.isNotBlank(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            String accessToken = extractBearerToken(request);

            String tokenType = JwtUtil.TOKEN_ACCESS_CLAIM_VALUE;
            if (AuthController.PATH_REFRESH_TOKEN.equals(request.getServletPath())) {
                tokenType = JwtUtil.TOKEN_REFRESH_CLAIM_VALUE;
            }

            JwtAuthenticationToken authRequest = new JwtAuthenticationToken(accessToken, tokenType);
            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
