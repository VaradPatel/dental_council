package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;

import javax.servlet.http.HttpServletResponse;

public interface IAuthService {

    LoginResponseTO successfulAuth(HttpServletResponse response);

    SessionResponseTo sessions(SessionRequestTo sessionRequestTo);

    LoginResponseTO successfulAuthRefreshToken(HttpServletResponse response);

    ResponseMessageTo logOut(String token);
}
