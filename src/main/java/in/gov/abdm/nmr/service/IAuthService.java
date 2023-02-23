package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.LoginResponseTO;

import javax.servlet.http.HttpServletResponse;

public interface IAuthService {

    LoginResponseTO successfulAuth(HttpServletResponse response);
}
