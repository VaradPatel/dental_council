package in.gov.abdm.nmr.service;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.dto.LoginResponseTO;

public interface IAuthService {

    LoginResponseTO successfulAuth(HttpServletResponse response);
}
