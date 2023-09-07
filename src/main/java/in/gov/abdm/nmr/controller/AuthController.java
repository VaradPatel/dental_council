package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.service.IAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_LOGIN;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_REFRESH_TOKEN;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_LOGIN_INTERNAL;

@RestController
public class AuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Autowired
    ChecksumUtil checksumUtil;

    @PostMapping(path = PATH_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseTO login(@RequestBody(required = false) LoginRequestTO loginRequestTO, HttpServletResponse response) {
        checksumUtil.validateChecksum();
        return authService.successfulAuth(response);
    }
    
    @Hidden
    @PostMapping(path = PATH_LOGIN_INTERNAL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseTO loginInternal(@RequestBody(required = false) LoginRequestTO loginRequestTO, HttpServletResponse response) {
        return authService.successfulAuth(response);
    }

    @PostMapping(path = PATH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public LoginResponseTO refreshToken(HttpServletResponse response) {
        checksumUtil.validateChecksum();
        return authService.successfulAuthRefreshToken(response);
    }

    @PostMapping(path = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionResponseTo sessions(@RequestBody SessionRequestTo sessionRequestTo) {
        checksumUtil.validateChecksum();
        return authService.sessions(sessionRequestTo);
    }

    @PostMapping(path = "/user/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo logout(@RequestHeader("Authorization") String token) {
        checksumUtil.validateChecksum();
        return authService.logOut(token);
    }
}
