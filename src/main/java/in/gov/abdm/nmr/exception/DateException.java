package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class DateException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public DateException() {
        super(NMRError.DATE_EXCEPTION.getCode(), NMRError.DATE_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public DateException(String message) {
        super(NMRError.DATE_EXCEPTION.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public DateException(String code, String message) {
        super(code, message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public DateException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }

}
