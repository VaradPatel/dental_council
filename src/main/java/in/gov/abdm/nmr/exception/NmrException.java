package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class NmrException extends Exception {

    private static final long serialVersionUID = -7017172960796901774L;

    private final HttpStatus status;

    public NmrException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public NmrException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
