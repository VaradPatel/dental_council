package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

public interface IAuthController {

    String login(LoginRequestTO loginRequestTO, HttpServletResponse response);

    String refreshToken(HttpServletResponse response);
}
