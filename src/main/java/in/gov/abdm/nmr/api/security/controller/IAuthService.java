package in.gov.abdm.nmr.api.security.controller;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.api.security.controller.to.LoginResponseTO;

public interface IAuthService {

    LoginResponseTO login(HttpServletResponse response);

    LoginResponseTO refreshToken(HttpServletResponse response);
}
