package in.gov.abdm.nmr.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NmrExceptionAdvice {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler({NmrException.class})
    public ResponseEntity<ErrorTO> handleApiException(HttpServletRequest req, Throwable ex) {
        NmrException nmrException = (NmrException) ex;
        ErrorTO error = new ErrorTO(new Date(), nmrException.getStatus().value(), ex.getMessage(), req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, nmrException.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorTO> handleException(HttpServletRequest req, Throwable ex) {
        LOGGER.error(ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occured", req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
