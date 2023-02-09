package in.gov.abdm.nmr.security.username_password;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;
import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.ICaptchaDaoService;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;

@Component
public class UserPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private String requestBodyString = null;

    private ObjectMapper objectMapper;

    private RsaUtil rsaUtil;

    private ICaptchaDaoService captchaDaoService;

    private AuthenticationEventPublisher authEventPublisher;
    
    private ISecurityAuditTrailDaoService securityAuditTrailDaoService;
    
    private Tracer tracer;

    @Autowired
    AuthenticationLockingService authenticationHandler;

    public UserPasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, RsaUtil rsaUtil, ICaptchaDaoService captchaDaoService, //
                                            AuthenticationEventPublisher authEventPublisher, ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        super();
        this.setRequiresAuthenticationRequestMatcher(ProtectedPaths.getLoginPathMatcher());
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        this.setAuthenticationFailureHandler((request, response, exception) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
        this.objectMapper = objectMapper;
        this.rsaUtil = rsaUtil;
        this.captchaDaoService = captchaDaoService;
        this.authEventPublisher = authEventPublisher;
        this.securityAuditTrailDaoService = securityAuditTrailDaoService;
        this.tracer = tracer;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserPasswordAuthenticationToken authRequest = UserPasswordAuthenticationToken.unauthenticated(null, null, null);
        try {
            requestBodyString = readRequestBody(request);
            LoginRequestTO requestBodyTO = objectMapper.readValue(requestBodyString, LoginRequestTO.class);
            authRequest = UserPasswordAuthenticationToken.unauthenticated(requestBodyTO.getUsername(), //
                    rsaUtil.decrypt(requestBodyTO.getPassword()), requestBodyTO.getUserType());
            authRequest.setDetails(createSecurityAuditTrail(request));

            if (!captchaDaoService.isCaptchaValidated(requestBodyTO.getCaptchaTransId())) {
                throw new AuthenticationServiceException("Invalid captcha");
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception occured while parsing username-password login request", e);
            throw new AuthenticationServiceException("Exception occured while parsing username-password login request");
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream requestInputStream = request.getInputStream();
        return new String(requestInputStream.available() > 0 ? requestInputStream.readAllBytes() : new byte[0]);
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

    private void publishAuthenticationFailure(HttpServletRequest request, AuthenticationException exception) throws IOException {
        String username = null;
        String payload = null;
        if (StringUtils.isNotBlank(requestBodyString)) {
            LoginRequestTO requestBodyTO = objectMapper.readValue(requestBodyString, LoginRequestTO.class);
            if (StringUtils.isNotBlank(requestBodyTO.getUsername())) {
                username = requestBodyTO.getUsername();
            } else {
                payload = requestBodyString;
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) //
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        publishAuthenticationFailure(request, failed);

        if (requestBodyString != null && !requestBodyString.isBlank()) {
            authenticationHandler.updateFailedAttemptsAndLockStatus(objectMapper.readValue(requestBodyString, LoginRequestTO.class).getUsername());
        }
    }
}
