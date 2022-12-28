package in.gov.abdm.nmr.api.security.username_password;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserTO;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

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
            throw new UsernameNotFoundException("Invalid username");
        }

        return new UserPassDetail(userDetail.getUsername(), userDetail.getPassword(), Collections.emptyList(), userDetail.getUserType().getId(), userDetail.getUserSubType().getId());
    }
}
