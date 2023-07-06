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
            LOGGER.info("Request : {}", request.getAuthType());
            LOGGER.info("Request : {}", request.getCharacterEncoding());
            LOGGER.info("Request : {}", request.getContentType());
            LOGGER.info("Request : {}", request.getContextPath());
            LOGGER.info("Request : {}", request.getLocalAddr());
            LOGGER.info("Request : {}", request.getLocalName());
            LOGGER.info("Request : {}", request.getMethod());
            LOGGER.info("Request : {}", request.getPathInfo());
            LOGGER.info("Request : {}", request.getPathTranslated());
            LOGGER.info("Request : {}", request.getProtocol());
            LOGGER.info("Request : {}", request.getQueryString());
            LOGGER.info("Request : {}", request.getRemoteAddr());
            LOGGER.info("Request : {}", request.getRemoteHost());
            LOGGER.info("Request : {}", request.getRemoteUser());
            LOGGER.info("Request : {}", request.getRequestedSessionId());
            LOGGER.info("Request : {}", request.getRequestURI());
            LOGGER.info("Request : {}", request.getRequestURL());
            LOGGER.info("Request : {}", request.getScheme());
            LOGGER.info("Request : {}", request.getServerName());
            LOGGER.info("Request : {}", request.getServletPath());
            LOGGER.info("Request : {}", request.getAttributeNames());
            LOGGER.info("Request : {}", request.getContentLength());
            LOGGER.info("Request : {}", request.getContentLengthLong());
            LOGGER.info("Request : {}", request.getCookies());
            LOGGER.info("Request : {}", request.getDispatcherType());
            LOGGER.info("Request : {}", request.getHeaderNames());
            LOGGER.info("Request : {}", request.getHttpServletMapping());
            LOGGER.info("Request : {}", request.getLocale());
            LOGGER.info("Request : {}", request.getLocales());
            LOGGER.info("Request : {}", request.getParameterMap());
            LOGGER.info("Request : {}", request.getParameterNames());
            LOGGER.info("Request : {}", request.getRemotePort());
            LOGGER.info("Request : {}", request.getServerPort());
            LOGGER.info("Request : {}", request.getServletContext());
            LOGGER.info("Request : {}", request.getSession());
            LOGGER.info("Request : {}", request.getTrailerFields());
            LOGGER.info("Request : {}", request.getUserPrincipal());
            
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).setAttribute(REQUEST_BODY, requestBody, RequestAttributes.SCOPE_REQUEST);

            LoginRequestTO requestBodyTO = convertRequestToDTO();
            authRequest = UserPasswordAuthenticationToken.unauthenticated(requestBodyTO.getUsername(),
                    rsaUtil.decrypt(requestBodyTO.getPassword()), requestBodyTO.getUserType(), requestBodyTO.getLoginType(), requestBodyTO.getOtpTransId());
            authRequest.setDetails(createSecurityAuditTrail(request));

            if (!captchaService.isCaptchaVerified(requestBodyTO.getCaptchaTransId())) {
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

    protected LoginRequestTO convertRequestToDTO() throws JsonProcessingException {
        return objectMapper.readValue(getRequestBody(), LoginRequestTO.class);
    }

    protected String readRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream requestInputStream = request.getInputStream();
        objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        return new String(requestInputStream.available() > 0 ? requestInputStream.readAllBytes() : new byte[0]);
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
            failed = new AuthenticationServiceException("Invalid Username or Password! Please enter valid username and password");
        }
        
        super.unsuccessfulAuthentication(request, response, failed);
        publishAuthenticationFailure(request, failed);
        
        String requestBodyString = getRequestBody();
        if (requestBodyString != null && !requestBodyString.isBlank()) {
            authenticationHandler.updateFailedAttemptsAndLockStatus(objectMapper.readValue(requestBodyString, LoginRequestTO.class).getUsername());
        }
    }
    
    private String getRequestBody() {
        return (String) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getAttribute(REQUEST_BODY, RequestAttributes.SCOPE_REQUEST);
    }
}
