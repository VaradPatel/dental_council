package in.gov.abdm.nmr.exception;

import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NmrExceptionAdvice {

    private static final Logger LOGGER = LogManager.getLogger();


    /**
     * Constant for Timestamp of generated response
     */
    private static final String RESPONSE_TIMESTAMP = "timestamp";

    /**
     * Constant for logging
     */
    private static final String FAILED_TO_SEND_REQUEST_TO_SMS_GATEWAY = "Failed to send request to sms gateway";
    /**
     * constant for error response
     */
    private static final String MESSAGE = "message";

    /**
     * constant for error response
     */
    private static final String CODE = "code";


    @ExceptionHandler({NmrException.class})
    public ResponseEntity<ErrorTO> handleApiException(HttpServletRequest req, Throwable ex) {
        NmrException nmrException = (NmrException) ex;
        ErrorTO error = new ErrorTO(new Date(), nmrException.getStatus().value(), ex.getMessage(), req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, nmrException.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorTO> handleSecurityException(HttpServletRequest req, Throwable ex) {
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.FORBIDDEN.value(), ex.getMessage(), req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorTO> handleException(HttpServletRequest req, Throwable ex){
        LOGGER.error("Unexpected error occured", ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occured", req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ InvalidRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTO> invalidRequest(HttpServletRequest req, Throwable ex) {
        LOGGER.error(ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.BAD_REQUEST.value(), "Invalid request", req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ WorkFlowException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTO> workflowExceptionHandler(HttpServletRequest req, Throwable ex) {
        LOGGER.error(ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }


    /**
     * Exception handler for {@link MethodArgumentNotValidException}.
     *
     * @param ex the exception to be handled
     * @return ErrorInfo object containing the error information
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode("ABDM-NMR-001");
        errorInfo.setMessage(HttpStatus.BAD_REQUEST.toString());
        List<DetailsTO> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            DetailsTO detailsTO = new DetailsTO();
            detailsTO.setCode("ABDM-NMR-001");
            detailsTO.setMessage("Invalid input");
            AttributeTO attributeTO = new AttributeTO();
            attributeTO.setKey(((FieldError) error).getField());
            attributeTO.setValue(error.getDefaultMessage());
            detailsTO.setAttribute(attributeTO);
            details.add(detailsTO);
        });
        errorInfo.setDetails(details);
        return errorInfo;
    }


    /**
     * Exception handler for OTP Exceptions
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OtpException.class)
    public Map<String, Object> oTPExceptionHandler(OtpException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put(RESPONSE_TIMESTAMP, LocalDate.now());
        LOGGER.error(e.getMessage());
        errorMap.put(MESSAGE, e.getMessage());
        return errorMap ;
    }

    /**
     * Exception handler for OTP Exceptions
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TemplateException.class)
    public Map<String, Object> templateExceptionHandler(TemplateException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put(RESPONSE_TIMESTAMP, LocalDate.now());
        LOGGER.error(e.getMessage());
        errorMap.put(MESSAGE, e.getMessage());
        return errorMap ;
    }
}
