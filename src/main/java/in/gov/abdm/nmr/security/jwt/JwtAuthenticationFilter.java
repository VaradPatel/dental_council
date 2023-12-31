package in.gov.abdm.nmr.security.jwt;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.redis.hash.BlacklistToken;
import in.gov.abdm.nmr.redis.repository.IBlacklistTokenRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import brave.Tracer;
import in.gov.abdm.nmr.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;
import in.gov.abdm.nmr.util.NMRConstants;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private AuthenticationEventPublisher authEventPublisher;

    private JwtUtil jwtUtil;

    private ISecurityAuditTrailDaoService securityAuditTrailDaoService;

    private Tracer tracer;

    @Autowired
    private IBlacklistTokenRepository blacklistTokenRepository;

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEventPublisher authEventPublisher, JwtUtil jwtUtil,
                                      ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        super(new OrRequestMatcher(ProtectedPaths.getProtectedPathsMatchers()), authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        this.setAuthenticationFailureHandler((request, response, exception) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
        this.authEventPublisher = authEventPublisher;
        this.jwtUtil = jwtUtil;
        this.securityAuditTrailDaoService = securityAuditTrailDaoService;
        this.tracer = tracer;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(null, JwtTypeEnum.ACCESS_TOKEN, null);
        try {
            if (StringUtils.isNotBlank(request.getHeader(HttpHeaders.AUTHORIZATION))) {
                String accessToken = extractBearerToken(request);

                if (accessToken == null) {
                    throw new AuthenticationServiceException(BEARER_TOKEN_ERROR_MSG);
                }

                isTokenBlacklisted(request);

                JwtTypeEnum tokenType = JwtTypeEnum.ACCESS_TOKEN;
                if (ProtectedPaths.PATH_REFRESH_TOKEN.equals(request.getServletPath())) {
                    tokenType = JwtTypeEnum.REFRESH_TOKEN;
                }

                authRequest = new JwtAuthenticationToken(accessToken, tokenType,null);
                authRequest.setDetails(createSecurityAuditTrail(request));
            } else {
                LOGGER.error(NO_BEARER_TOKEN_ERROR_MSG);
                throw new AuthenticationServiceException(NO_BEARER_TOKEN_ERROR_MSG);
            }
            
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_IN_PARSING_BEARER_TOKEN, e);
            throw new AuthenticationServiceException(EXCEPTION_IN_PARSING_BEARER_TOKEN);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            return authHeader.substring(authHeader.indexOf(" ") + 1, authHeader.length());
        }
        return null;
    }

    private SecurityAuditTrail createSecurityAuditTrail(HttpServletRequest request) {
        SecurityAuditTrail securityAuditTrail = new SecurityAuditTrail();
        String ipAddress = request.getHeader("X-Real-IP");
        securityAuditTrail.setIpAddress(StringUtils.isNotBlank(ipAddress) ? ipAddress : request.getRemoteAddr());
        securityAuditTrail.setUserAgent(request.getHeader("User-Agent"));
        securityAuditTrail.setEndpoint(request.getRequestURI());
        securityAuditTrail.setProcessId(System.getProperty("PID"));
        securityAuditTrail.setHttpMethod(request.getMethod());
        return securityAuditTrail;
    }

    private void publishAuthenticationFailure(HttpServletRequest request, AuthenticationException exception) {
        String username = null;
        String payload = null;
        if (StringUtils.isNotBlank(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            String jwtToken = extractBearerToken(request);
            if (exception instanceof InvalidBearerTokenException) {
                payload = jwtToken;
            } else {
                username = jwtUtil.decodeToken(jwtToken).getSubject();
            }
        }

        SecurityAuditTrail securityAuditTrail = securityAuditTrailDaoService.findByCorrelationId(tracer.currentSpan().context().traceIdString());
        if (securityAuditTrail != null) {
            securityAuditTrail.setUsername(username);
            securityAuditTrail.setPayload(payload);
        } else {
            securityAuditTrail = createSecurityAuditTrail(request);
            securityAuditTrail.setUsername(username);
            securityAuditTrail.setPayload(payload);
        }

        UsernamePasswordAuthenticationToken failureAuthRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, null);
        failureAuthRequest.setDetails(securityAuditTrail);
        authEventPublisher.publishAuthenticationFailure(exception, failureAuthRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        if(authResult.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(RoleConstants.ROLE_PREFIX + RoleConstants.SYSTEM)) &&
                !NMRConstants.HEALTH_PROFESSIONAL_ACTION.equals(request.getServletPath())) {
            throw new AuthenticationServiceException(NOT_ALLOWED_ERROR_MSG);
        }
        
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        publishAuthenticationFailure(request, failed);
    }

    private void isTokenBlacklisted(HttpServletRequest request) {
        String accessToken = extractBearerToken(request);
        if(accessToken!=null) {

            String[] token = accessToken.split("\\.");
            BlacklistToken blacklistToken=null;
            blacklistToken = blacklistTokenRepository.findById(token[2]).orElse(null);

            if (!Objects.isNull(blacklistToken)) {
                throw new AuthenticationServiceException(EXPIRED_TOKEN_ERROR_MSG);
            }
        }
        else {
            throw new AuthenticationServiceException(NO_BEARER_TOKEN_ERROR_MSG);
        }
    }
}
