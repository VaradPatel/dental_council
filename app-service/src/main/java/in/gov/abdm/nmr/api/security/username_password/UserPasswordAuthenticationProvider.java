package in.gov.abdm.nmr.api.security.username_password;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    public UserPasswordAuthenticationProvider(UserPasswordDetailsService userPasswordDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super();
        this.setUserDetailsService(userPasswordDetailsService);
        this.setPasswordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPasswordAuthenticationToken userPassAuthToken = (UserPasswordAuthenticationToken) authentication;

        UserPassDetail userDetail = (UserPassDetail) this.getUserDetailsService().loadUserByUsername(userPassAuthToken.getName());
        if (userDetail == null) {
            throw new UsernameNotFoundException("Invalid username");
        }

        if (userPassAuthToken.getUserType() == null || !userDetail.getUserType().equals(userPassAuthToken.getUserType())) {
            LOGGER.error("Usertype and credentials do not match");
            userPassAuthToken.eraseCredentials();
            throw new UsernameNotFoundException("Invalid username");
        }

        return super.authenticate(userPassAuthToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UserPasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
