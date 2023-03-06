package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;

import javax.servlet.http.HttpServletResponse;

public interface IAuthService {

    LoginResponseTO successfulAuth(HttpServletResponse response);

    SessionResponseTo sessions(SessionRequestTo sessionRequestTo) throws JsonProcessingException;
}
