package in.gov.abdm.nmr.security.username_password;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationLockHandler {

    @Autowired
    IUserService userService;
    @Autowired
    UserDaoServiceImpl userDaoService;

    private static final Logger LOGGER = LogManager.getLogger();

    public void onAuthenticationFailure(String username) throws AuthenticationServiceException {

        User user = userDaoService.findUserDetailByUsername(username);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < NMRConstants.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.increaseFailedAttempts(user);
                    userService.lock(user);
                    LOGGER.error(NMRConstants.ACCOUNT_LOCKED_MESSAGE);
                }
            } else {
                if (userService.unlockWhenTimeExpired(user)) {
                    LOGGER.error(NMRConstants.ACCOUNT_UNLOCKED_MESSAGE);
                } else {
                    LOGGER.error(NMRConstants.ACCOUNT_LOCKED_MESSAGE);
                }
            }
        }
    }

    public boolean onAuthenticationSuccess(String username) throws AuthenticationServiceException {
        User user = userDaoService.findUserDetailByUsername(username);

        if (user.isAccountNonLocked()) {
            return true;
        } else {
            return userService.unlockWhenTimeExpired(user);

        }
    }
}