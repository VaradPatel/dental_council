package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class InvalidIdException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public InvalidIdException() {
        super(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidIdException(String message) {
        super(NMRError.INVALID_ID_EXCEPTION.getCode(), message, HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidIdException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidIdException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }

}