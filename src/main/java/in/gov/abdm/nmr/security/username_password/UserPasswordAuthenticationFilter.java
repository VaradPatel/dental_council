package in.gov.abdm.nmr.security.username_password;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.ICaptchaDaoService;

@Component
public class UserPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private ObjectMapper objectMapper;

    private RsaUtil rsaUtil;

    private ICaptchaDaoService captchaDaoService;

    public UserPasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, RsaUtil rsaUtil, ICaptchaDaoService captchaDaoService) {
        super();
        this.setRequiresAuthenticationRequestMatcher(ProtectedPaths.getLoginPathMatcher());
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        this.objectMapper = objectMapper;
        this.rsaUtil = rsaUtil;
        this.captchaDaoService = captchaDaoService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        try {
            LoginRequestTO requestBodyTO = readRequestBody(request);
            UserPasswordAuthenticationToken authRequest = UserPasswordAuthenticationToken.unauthenticated(requestBodyTO.getUsername(), //
                    rsaUtil.decrypt(requestBodyTO.getPassword()), requestBodyTO.getUserType());

            setDetails(request, authRequest);

            if (!captchaDaoService.isCaptchaValidated(requestBodyTO.getCaptchaTransId())) {
                throw new AuthenticationServiceException("Invalid captcha");
            }

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            LOGGER.error("Exception occured while parsing username-password login request", e);
        }
        throw new AuthenticationServiceException("Unable to parse login request");
    }

    private LoginRequestTO readRequestBody(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(new String(request.getInputStream().readAllBytes()), LoginRequestTO.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) //
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}