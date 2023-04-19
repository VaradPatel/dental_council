package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class OtpException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public OtpException() {
        super(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage(), HttpStatus.UNAUTHORIZED.toString());
    }

    public OtpException(String message) {
        super(NMRError.OTP_INVALID.getCode(), message, HttpStatus.UNAUTHORIZED.toString());
    }

    public OtpException(String code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED.toString());
    }

    public OtpException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }

}
