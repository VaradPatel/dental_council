package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class WorkFlowException extends Exception {

    private final HttpStatus status;
    public WorkFlowException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public WorkFlowException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
