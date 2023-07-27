package in.gov.abdm.nmr.security.username_password;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;
import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.enums.LoginTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;

import static in.gov.abdm.nmr.util.NMRConstants.EXCEPTION_IN_PARSING_USERNAME_PASSWORD;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_ALLOWED_ERROR_MSG;

@Component
public class UserPasswordAuthenticationFilterInternal extends UserPasswordAuthenticationFilter {

    private static final Logger LOGGER = LogManager.getLogger();
    
    private static final List<BigInteger> ALLOWED_LOGIN_TYPES = Arrays.asList(LoginTypeEnum.USERNAME_PASSWORD.getCode());
    
    private static final List<BigInteger> ALLOWED_USER_TYPES = Arrays.asList(UserTypeEnum.SYSTEM.getId());

    public UserPasswordAuthenticationFilterInternal(AuthenticationManager authenticationManager, ObjectMapper objectMapper, AuthenticationEventPublisher authEventPublisher,
                                                    ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        super(authenticationManager, objectMapper, null, null, authEventPublisher, securityAuditTrailDaoService, tracer);
        this.setRequiresAuthenticationRequestMatcher(ProtectedPaths.getInternalLoginPathMatcher());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserPasswordAuthenticationToken authRequest = UserPasswordAuthenticationToken.unauthenticated(null, null, null, null, null);
        try {
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).setAttribute(UserPasswordAuthenticationFilter.REQUEST_BODY, readRequestBody(request), RequestAttributes.SCOPE_REQUEST);

            LoginRequestTO requestBodyTO = convertRequestToDTO();
            if(!ALLOWED_LOGIN_TYPES.contains(requestBodyTO.getLoginType())) {
                throw new AuthenticationServiceException(NOT_ALLOWED_ERROR_MSG);
            }
            
            if(!ALLOWED_USER_TYPES.contains(requestBodyTO.getUserType())) {
                throw new AuthenticationServiceException(NOT_ALLOWED_ERROR_MSG);
            }
            
            authRequest = UserPasswordAuthenticationToken.unauthenticated(requestBodyTO.getUsername(), requestBodyTO.getPassword(), requestBodyTO.getUserType(),
                    requestBodyTO.getLoginType(), null);
            authRequest.setDetails(createSecurityAuditTrail(request));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_IN_PARSING_USERNAME_PASSWORD, e);
            throw new AuthenticationServiceException(EXCEPTION_IN_PARSING_USERNAME_PASSWORD);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
