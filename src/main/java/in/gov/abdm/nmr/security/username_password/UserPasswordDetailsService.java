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
import in.gov.abdm.nmr.dto.UserTO;
import in.gov.abdm.nmr.service.IUserDaoService;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    AuthenticationLockingService authenticationHandler;

    private IUserDaoService userDetailService;

    public UserPasswordDetailsService(IUserDaoService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSearchTO userDetailSearchTO = new UserSearchTO();
        userDetailSearchTO.setUsername(username);

        UserTO userDetail = userDetailService.searchUserDetail(userDetailSearchTO);
        if (userDetail == null) {
            LOGGER.error("Invalid username");
            throw new UsernameNotFoundException("Invalid username");
        }

        boolean isAccountNonLocked= authenticationHandler.checkAndUpdateLockStatus(username);
        return new UserPassDetail(userDetail.getUsername(), userDetail.getPassword(), Collections.emptyList(), userDetail.getUserType().getId(),isAccountNonLocked);
    }
}
