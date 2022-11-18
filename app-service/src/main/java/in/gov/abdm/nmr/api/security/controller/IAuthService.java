package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

public interface IAuthService {

    void login(HttpServletResponse response);

    void refreshToken(HttpServletResponse response);
}
