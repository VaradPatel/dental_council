package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NoDataFoundException is a custom exception class that extends the {@link RuntimeException}.
 * This exception class is used to indicate when a requested data could not be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataFoundException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public NoDataFoundException() {
        super(NMRError.NO_DATA_FOUND.getCode(), NMRError.NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    public NoDataFoundException(String message) {
        super(NMRError.NO_DATA_FOUND.getCode(), message, HttpStatus.NOT_FOUND.toString());
    }

    public NoDataFoundException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.NOT_FOUND.toString());
    }

    public NoDataFoundException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }
}