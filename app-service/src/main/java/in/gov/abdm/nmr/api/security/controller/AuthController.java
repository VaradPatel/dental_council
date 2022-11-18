package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements IAuthController {

    public static final String PATH_LOGIN = "/login";
    public static final String PATH_REFRESH_TOKEN = "/refreshToken";
    public static final String PATH_TEST = "/test";

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        super();
        this.authService = authService;
    }

    @Override
    @PostMapping(path = PATH_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody(required = false) LoginRequestTO loginRequestTO, HttpServletResponse response) {
        authService.login(response);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    @PostMapping(path = PATH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String refreshToken(HttpServletResponse response) {
        authService.refreshToken(response);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping(path = PATH_TEST)
    public String test() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
