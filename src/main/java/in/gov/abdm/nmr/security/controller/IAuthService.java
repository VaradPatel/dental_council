package in.gov.abdm.nmr.security.controller;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.security.controller.to.LoginResponseTO;

public interface IAuthService {

    LoginResponseTO login(HttpServletResponse response);

    LoginResponseTO refreshToken(HttpServletResponse response);
}
