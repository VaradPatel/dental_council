package in.gov.abdm.nmr.api.exception;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NMRExceptionAdvice {

	private static Map<Class<? extends Throwable>, ExceptionTO> statusCodeMap = new HashMap<>();

	static {		
		statusCodeMap.put(InvalidRequestException.class,
				new ExceptionTO(HttpStatus.BAD_REQUEST, ""));
	}

	@ExceptionHandler({ InvalidRequestException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorTO> handleException(HttpServletRequest req, Throwable ex) {
		ExceptionTO exceptionTO = statusCodeMap.getOrDefault(ex.getClass(), null);
		ErrorTO error = new ErrorTO(new Date(), exceptionTO.getStatus().value(),
				(ex.getMessage() != null && !ex.getMessage().isBlank()) ? ex.getMessage() : exceptionTO.getMessage(),
				req.getServletPath(), exceptionTO.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(error, headers, exceptionTO.getStatus());
	}

}
