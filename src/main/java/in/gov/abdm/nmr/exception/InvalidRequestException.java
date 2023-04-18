package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public InvalidRequestException() {
        super(NMRError.INVALID_REQUEST.getCode(), NMRError.INVALID_REQUEST.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidRequestException(String message) {
        super(NMRError.INVALID_REQUEST.getCode(), message, HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidRequestException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST.toString());
    }
}
