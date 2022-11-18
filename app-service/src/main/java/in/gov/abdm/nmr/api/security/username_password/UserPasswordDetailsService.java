package in.gov.abdm.nmr.api.security.username_password;

import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import in.gov.abdm.nmr.api.ext.dto.UserDetailTO;

@Component
public class UserPasswordDetailsService implements UserDetailsService {

    private WebClient webClient;

    public UserPasswordDetailsService(WebClient webClient) {
        super();
        this.webClient = webClient;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDetailTO userDetail = webClient.method(HttpMethod.GET).uri("/userDetails/username").bodyValue(id).retrieve().bodyToMono(UserDetailTO.class) //
                .blockOptional().orElse(new UserDetailTO());
        return User.builder().username(userDetail.getUsername()).password(userDetail.getPassword()).authorities(Collections.emptyList()).build();
    }
}
