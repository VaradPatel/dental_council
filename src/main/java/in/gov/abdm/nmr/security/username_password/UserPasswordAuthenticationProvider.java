package in.gov.abdm.nmr.security.username_password;

import java.security.GeneralSecurityException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.enums.LoginTypeEnum;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.service.IOtpService;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USERNAME_ERROR_MSG;

@Component
@Slf4j
public class UserPasswordAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    
    private IOtpService otpService;

    public UserPasswordAuthenticationProvider(UserPasswordDetailsService userPasswordDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, IOtpService otpService) {
        super();
        this.setUserDetailsService(userPasswordDetailsService);
        this.setPasswordEncoder(bCryptPasswordEncoder);
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPasswordAuthenticationToken userPassAuthToken = (UserPasswordAuthenticationToken) authentication;
        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
        UserPassDetail userDetail = (UserPassDetail) this.getUserDetailsService().loadUserByUsername(userPassAuthToken.getName());
        if (userPassAuthToken.getUserType() == null || !userDetail.getUserType().equals(userPassAuthToken.getUserType())) {
            LOGGER.error("Usertype and credentials do not match");
            userPassAuthToken.eraseCredentials();
            throw new AuthenticationServiceException(INVALID_USERNAME_ERROR_MSG);
        }

        if (LoginTypeEnum.MOBILE_OTP.getCode().equals(userPassAuthToken.getLoginType()) || LoginTypeEnum.NMR_ID_OTP.getCode().equals(userPassAuthToken.getLoginType())) {
            try {
                String contact = "";
                if (LoginTypeEnum.MOBILE_OTP.getCode().equals(userPassAuthToken.getLoginType())) {
                    contact = (String) userPassAuthToken.getPrincipal();
                }
                if (LoginTypeEnum.NMR_ID_OTP.getCode().equals(userPassAuthToken.getLoginType())) {
                    contact = userDetail.getMobileNumber();
                }

                OtpValidateRequestTo otpValidateRequestTo = new OtpValidateRequestTo(userPassAuthToken.getOtpTransactionId(), contact,
                        NotificationType.SMS.getNotificationType(), (String) userPassAuthToken.getCredentials());
                otpService.validateOtp(otpValidateRequestTo, true);
                return UserPasswordAuthenticationToken.authenticated(userPassAuthToken.getPrincipal(), userPassAuthToken.getCredentials(), Collections.emptyList(), userPassAuthToken.getUserType(), userPassAuthToken.getDetails());
            } catch (OtpException e) {
                log.error("Exception occurred while authentication.", e);
                throw new InternalAuthenticationServiceException(e.getMessage());
            } catch (GeneralSecurityException e) {
                log.error("Exception occurred while authentication.", e);
            }
        }

        super.authenticate(userPassAuthToken);
        return UserPasswordAuthenticationToken.authenticated(userPassAuthToken.getPrincipal(), userPassAuthToken.getCredentials(), Collections.emptyList(), userPassAuthToken.getUserType(), userPassAuthToken.getDetails());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UserPasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
