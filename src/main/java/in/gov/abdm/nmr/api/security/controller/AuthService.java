package in.gov.abdm.nmr.api.security.controller;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.security.jwt.JwtTypeEnum;
import in.gov.abdm.nmr.api.security.jwt.JwtUtil;

@Service
public class AuthService implements IAuthService {

    private JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(HttpServletResponse response) {
        return generateAccessAndRefreshToken(response);
    }

    @Override
    public String refreshToken(HttpServletResponse response) {
        return generateAccessAndRefreshToken(response);
    }

    private String generateAccessAndRefreshToken(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN));
        response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN));

        return username;
    }
}
