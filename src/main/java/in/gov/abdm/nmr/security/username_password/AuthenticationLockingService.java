package in.gov.abdm.nmr.security.username_password;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AuthenticationLockingService {

    @Autowired
    UserDaoServiceImpl userDaoService;

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Increments failed attempts and update lock status
     * @param username
     * @throws AuthenticationServiceException
     */
    public void updateFailedAttemptsAndLockStatus(String username) throws AuthenticationServiceException {

        User user = userDaoService.findByUsername(username);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < NMRConstants.MAX_FAILED_ATTEMPTS - 1) {
                    increaseFailedAttempts(user);
                } else {
                    increaseFailedAttempts(user);
                    lock(user);
                    LOGGER.error(NMRConstants.ACCOUNT_LOCKED_MESSAGE);
                }
            } else {
                if (unlockWhenTimeExpired(user)) {
                    LOGGER.error(NMRConstants.ACCOUNT_UNLOCKED_MESSAGE);
                } else {
                    LOGGER.error(NMRConstants.ACCOUNT_LOCKED_MESSAGE);
                }
            }
        }
    }

    /**
     * Checks the status of locking and updates if needed
     * @param username to find user
     * @return true/false
     * @throws AuthenticationServiceException
     */
    public boolean checkAndUpdateLockStatus(String username) throws AuthenticationServiceException {
        User user = userDaoService.findByUsername(username);

        if (user.isAccountNonLocked()) {
            return true;
        } else {
            return unlockWhenTimeExpired(user);

        }
    }

    /**
     * Increases login failed attempt by 1
     *
     * @param user user class object
     */
    public void increaseFailedAttempts(User user) {
        user.setFailedAttempt(user.getFailedAttempt() + 1);
        userDaoService.save(user);
    }

    /**
     * Sets user status to lock and create entry for lock time
     *
     * @param user class object
     */
    public void lock(User user) {

        user.setAccountNonLocked(false);
        user.setLockTime(Timestamp.valueOf(LocalDateTime.now()));
        userDaoService.save(user);
    }

    /**
     * Unlocked user after expiry time
     *
     * @param user class object
     * @return success/failure of account unlocking
     */
    public boolean unlockWhenTimeExpired(User user) {

        LocalDateTime timeWithDuration=user.getLockTime().toLocalDateTime().plusHours(NMRConstants.LOCK_TIME_DURATION);

        if (timeWithDuration.isBefore(LocalDateTime.now())) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userDaoService.save(user);
            return true;
        }
        return false;
    }
}