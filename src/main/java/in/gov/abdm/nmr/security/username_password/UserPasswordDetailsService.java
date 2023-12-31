package in.gov.abdm.nmr.security.username_password;

import java.math.BigInteger;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.IUserDaoService;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USERNAME_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_ALLOWED_ERROR_MSG;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    AuthenticationLockingService authenticationHandler;

    private IUserDaoService userDaoService;

    public UserPasswordDetailsService(IUserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSearchTO userDetailSearchTO = new UserSearchTO();
        userDetailSearchTO.setUsername(username);

        BigInteger userType=((UserPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserType();

        User user = userDaoService.findByUsername(username, userType);
        if (user == null) {
            LOGGER.error(INVALID_USERNAME_ERROR_MSG);
            throw new UsernameNotFoundException(INVALID_USERNAME_ERROR_MSG);
        }
        
        if (StringUtils.isBlank(user.getPassword())) {
            LOGGER.error(NOT_ALLOWED_ERROR_MSG);
            throw new AuthenticationServiceException(NOT_ALLOWED_ERROR_MSG);
        }

        boolean isAccountNonLocked = authenticationHandler.checkAndUpdateLockStatus(username, userType);
        return new UserPassDetail(username, user.getMobileNumber(), user.getPassword(), Collections.emptyList(), user.getUserType().getId(), isAccountNonLocked);
    }
}
