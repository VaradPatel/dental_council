package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class ResourceExistsException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public ResourceExistsException() {
        super(NMRError.RESOURCE_EXISTS_EXCEPTION.getCode(), NMRError.RESOURCE_EXISTS_EXCEPTION.getMessage(), HttpStatus.ALREADY_REPORTED.toString());
    }

    public ResourceExistsException(String message) {
        super(NMRError.RESOURCE_EXISTS_EXCEPTION.getCode(), message, HttpStatus.ALREADY_REPORTED.toString());
    }

    public ResourceExistsException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.ALREADY_REPORTED.toString());
    }


    public ResourceExistsException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }
}