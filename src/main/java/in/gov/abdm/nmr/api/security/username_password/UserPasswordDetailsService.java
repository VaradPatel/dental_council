package in.gov.abdm.nmr.api.security.username_password;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.db.sql.domain.user_detail.IUserDetailService;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailTO;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

    private IUserDetailService userDetailService;

    public UserPasswordDetailsService(IUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailSearchTO userDetailSearchTO = new UserDetailSearchTO();
        userDetailSearchTO.setUsername(username);

        UserDetailTO userDetail = userDetailService.searchUserDetail(userDetailSearchTO);
        if (userDetail == null) {
            throw new UsernameNotFoundException("Invalid username");
        }

        return new UserPassDetail(userDetail.getUsername(), userDetail.getPassword(), Collections.emptyList(), userDetail.getUserType().getCode());
    }
}
