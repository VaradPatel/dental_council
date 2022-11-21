package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

public interface IAuthService {

    String login(HttpServletResponse response);

    String refreshToken(HttpServletResponse response);
}
