package in.gov.abdm.nmr.security.username_password;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.IUserDaoService;

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

        User user = userDaoService.findByUsername(username);
        if (user == null) {
            LOGGER.error("Invalid username");
            throw new UsernameNotFoundException("Invalid username");
        }

        boolean isAccountNonLocked = authenticationHandler.checkAndUpdateLockStatus(username);
        return new UserPassDetail(username, user.getMobileNumber(), user.getPassword(), Collections.emptyList(), user.getUserType().getId(), isAccountNonLocked);
    }
}
