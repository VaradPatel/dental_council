package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super(NMRError.NOT_FOUND_EXCEPTION.getCode(), NMRError.NOT_FOUND_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    public NotFoundException(String message) {
        super(NMRError.NOT_FOUND_EXCEPTION.getCode(), message, HttpStatus.NOT_FOUND.toString());
    }

    public NotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND.toString());
    }

    public NotFoundException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }


}