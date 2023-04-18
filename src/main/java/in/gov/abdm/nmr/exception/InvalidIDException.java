package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class InvalidIDException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public InvalidIDException() {
        super(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidIDException(String message) {
        super(NMRError.INVALID_ID_EXCEPTION.getCode(), message, HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidIDException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.BAD_REQUEST.toString());
    }
}