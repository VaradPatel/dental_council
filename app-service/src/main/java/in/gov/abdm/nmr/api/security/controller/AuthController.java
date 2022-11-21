package in.gov.abdm.nmr.api.security.controller;

import static in.gov.abdm.nmr.api.security.common.ProtectedPaths.PATH_LOGIN;
import static in.gov.abdm.nmr.api.security.common.ProtectedPaths.PATH_REFRESH_TOKEN;
import static in.gov.abdm.nmr.api.security.common.ProtectedPaths.PATH_TEST;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements IAuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping(path = PATH_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody(required = false) LoginRequestTO loginRequestTO, HttpServletResponse response) {
        return authService.login(response);
    }

    @Override
    @PostMapping(path = PATH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String refreshToken(HttpServletResponse response) {
        return authService.refreshToken(response);
    }

    @GetMapping(path = PATH_TEST)
    public String test() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
