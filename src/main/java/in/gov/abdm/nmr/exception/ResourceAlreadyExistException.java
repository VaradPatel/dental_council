package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public ResourceAlreadyExistException() {
        super(NMRError.ALREADY_EXIST_EXCEPTION.getCode(), NMRError.ALREADY_EXIST_EXCEPTION.getMessage(), HttpStatus.ALREADY_REPORTED.toString());
    }

    public ResourceAlreadyExistException(String message) {
        super(NMRError.ALREADY_EXIST_EXCEPTION.getCode(), message, HttpStatus.ALREADY_REPORTED.toString());
    }

    public ResourceAlreadyExistException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.ALREADY_REPORTED.toString());
    }
}