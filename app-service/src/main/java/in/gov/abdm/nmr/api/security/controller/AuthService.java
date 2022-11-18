package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.security.jwt.JwtUtil;

@Service
public class AuthService implements IAuthService {

    private JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void login(HttpServletResponse response) {
        generateAccessAndRefreshToken(response);
    }

    @Override
    public void refreshToken(HttpServletResponse response) {
        generateAccessAndRefreshToken(response);
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        response.setHeader("access-token", jwtUtil.generateToken(username, JwtUtil.TOKEN_ACCESS_CLAIM_VALUE));
        response.setHeader("refresh-token", jwtUtil.generateToken(username, JwtUtil.TOKEN_REFRESH_CLAIM_VALUE));
    }
}
