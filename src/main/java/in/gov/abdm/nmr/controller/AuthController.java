package in.gov.abdm.nmr.controller;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_LOGIN;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_REFRESH_TOKEN;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.service.IAuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class AuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = PATH_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseTO login(@RequestBody(required = false) LoginRequestTO loginRequestTO, HttpServletResponse response) {
        return authService.successfulAuth(response);
    }

    @PostMapping(path = PATH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @SecurityRequirement(name = "bearerAuth")
    public LoginResponseTO refreshToken(HttpServletResponse response) {
        return authService.successfulAuth(response);
    }
}
