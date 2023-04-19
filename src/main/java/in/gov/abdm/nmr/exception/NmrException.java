package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class NmrException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public NmrException() {
        super(NMRError.NMR_EXCEPTION.getCode(), NMRError.NMR_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public NmrException(String message) {
        super(NMRError.NMR_EXCEPTION.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public NmrException(String code, String message) {
        super(code, message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public NmrException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }
}