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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;
import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.ICaptchaService;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Component
public class UserPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    protected static final String REQUEST_BODY = "request-body";

    private static final Logger LOGGER = LogManager.getLogger();

    private ObjectMapper objectMapper;

    private RsaUtil rsaUtil;

    private ICaptchaService captchaService;

    private AuthenticationEventPublisher authEventPublisher;
    
    private ISecurityAuditTrailDaoService securityAuditTrailDaoService;
    
    private Tracer tracer;

    @Autowired
    AuthenticationLockingService authenticationHandler;

    public UserPasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, RsaUtil rsaUtil, ICaptchaService captchaService,
                                            AuthenticationEventPublisher authEventPublisher, ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        super();
        this.setRequiresAuthenticationRequestMatcher(ProtectedPaths.getLoginPathMatcher());
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        this.setAuthenticationFailureHandler((request, response, exception) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
        this.objectMapper = objectMapper;
        this.rsaUtil = rsaUtil;
        this.captchaService = captchaService;
        this.authEventPublisher = authEventPublisher;
        this.securityAuditTrailDaoService = securityAuditTrailDaoService;
        this.tracer = tracer;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserPasswordAuthenticationToken authRequest = UserPasswordAuthenticationToken.unauthenticated(null, null, null, null, null);
        try {
            String requestBody = readRequestBody(request);
            LOGGER.info("Read request body : {}", requestBody);
            
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).setAttribute(REQUEST_BODY, requestBody, RequestAttributes.SCOPE_REQUEST);

            LoginRequestTO requestBodyTO = convertRequestToDTO();
            authRequest = UserPasswordAuthenticationToken.unauthenticated(requestBodyTO.getUsername(),
                    rsaUtil.decrypt(requestBodyTO.getPassword()), requestBodyTO.getUserType(), requestBodyTO.getLoginType(), requestBodyTO.getOtpTransId());
            authRequest.setDetails(createSecurityAuditTrail(request));

            if (!captchaService.isCaptchaVerified(requestBodyTO.getCaptchaTransId())) {
                throw new AuthenticationServiceException(INVALID_CAPTCHA_ERROR_MSG);
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_IN_PARSING_USERNAME_PASSWORD, e);
            throw new AuthenticationServiceException(EXCEPTION_IN_PARSING_USERNAME_PASSWORD);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected LoginRequestTO convertRequestToDTO() throws JsonProcessingException {
        return objectMapper.readValue(getRequestBody(), LoginRequestTO.class);
    }

    protected String readRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream requestInputStream = request.getInputStream();
        objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        return new String(requestInputStream.readAllBytes());
    }

    protected SecurityAuditTrail createSecurityAuditTrail(HttpServletRequest request) {
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
        
        String requestBodyString = getRequestBody();
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if(failed instanceof BadCredentialsException) {
            failed = new AuthenticationServiceException(INCORRECT_USERNAME_OR_PASSWORD_ERROR_MSG);
        }
        
        super.unsuccessfulAuthentication(request, response, failed);
        publishAuthenticationFailure(request, failed);
        
        String requestBodyString = getRequestBody();
        if (requestBodyString != null && !requestBodyString.isBlank()) {
            LoginRequestTO loginRequestTO=objectMapper.readValue(requestBodyString, LoginRequestTO.class);
            authenticationHandler.updateFailedAttemptsAndLockStatus(loginRequestTO.getUsername(),loginRequestTO.getUserType());

        }
    }
    
    private String getRequestBody() {
        return (String) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getAttribute(REQUEST_BODY, RequestAttributes.SCOPE_REQUEST);
    }
}
