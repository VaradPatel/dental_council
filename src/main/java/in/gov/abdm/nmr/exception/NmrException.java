package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class NmrException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public NmrException() {
        super(NMRError.NMR_EXCEPTION.getCode(), NMRError.NMR_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public NmrException(String message) {
        super(NMRError.NMR_EXCEPTION.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public NmrException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}