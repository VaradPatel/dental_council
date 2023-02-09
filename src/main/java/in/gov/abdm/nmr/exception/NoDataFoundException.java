package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NoDataFoundException is a custom exception class that extends the {@link RuntimeException}.
 * This exception class is used to indicate when a requested data could not be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
    }
}

