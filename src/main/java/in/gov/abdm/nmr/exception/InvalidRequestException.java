package in.gov.abdm.nmr.exception;

import java.io.IOException;

public class InvalidRequestException extends IOException {

	private static final long serialVersionUID = -7523737201585567497L;

	public InvalidRequestException() {
		super();
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(Throwable cause) {
		super(cause);
	}
}
