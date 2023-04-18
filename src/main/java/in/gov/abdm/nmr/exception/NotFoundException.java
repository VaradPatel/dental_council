package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public NotFoundException() {
        super(NMRError.NOT_FOUND_EXCEPTION.getCode(), NMRError.NOT_FOUND_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    public NotFoundException(String message) {
        super(NMRError.NOT_FOUND_EXCEPTION.getCode(), message, HttpStatus.NOT_FOUND.toString());
    }

    public NotFoundException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.NOT_FOUND.toString());
    }
}