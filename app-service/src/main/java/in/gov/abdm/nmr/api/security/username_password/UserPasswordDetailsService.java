package in.gov.abdm.nmr.api.security.username_password;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.api.ext.user_detail.IUserDetailService;
import in.gov.abdm.nmr.api.ext.user_detail.UserDetailTO;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

    private IUserDetailService userDetailService;

    public UserPasswordDetailsService(IUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailTO userDetail = userDetailService.findByUsername(username);
        if (userDetail == null) {
            throw new UsernameNotFoundException("Invalid username");
        }

        return new UserPassDetail(userDetail.getUsername(), userDetail.getPassword(), Collections.emptyList(), userDetail.getUserType().getId());
    }
}
