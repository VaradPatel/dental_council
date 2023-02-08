package in.gov.abdm.nmr.exception;

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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * NmrExceptionAdvice is a class that provides advice for handling exceptions in a RESTful service.
 */
@RestControllerAdvice
public class NmrExceptionAdvice {

    /**
     * The LOGGER is a private static final logger object that is used to log messages and events
     * in the application. It is initialized using the LogManager.getLogger() method.
     */
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
    public ResponseEntity<ErrorTO> handleException(HttpServletRequest req, Throwable ex) {
        LOGGER.error("Unexpected error occured", ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occured", req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTO> invalidRequest(HttpServletRequest req, Throwable ex) {
        LOGGER.error(ex);
        ErrorTO error = new ErrorTO(new Date(), HttpStatus.BAD_REQUEST.value(), "Invalid request", req.getServletPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({WorkFlowException.class})
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
        errorInfo.setCode(INPUT_VALIDATION_ERROR_CODE);
        errorInfo.setMessage(HttpStatus.BAD_REQUEST.toString());
        List<DetailsTO> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            DetailsTO detailsTO = new DetailsTO();
            detailsTO.setCode(INPUT_VALIDATION_INTERNAL_ERROR_CODE);
            detailsTO.setMessage(INVALID_INPUT_ERROR_MSG);
            AttributeTO attributeTO = new AttributeTO();
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            attributeTO.setKey(field);
            String formattedMessage = MessageFormat.format(defaultMessage, field);
            attributeTO.setValue(formattedMessage);
            detailsTO.setAttribute(attributeTO);
            details.add(detailsTO);
        });
        errorInfo.setDetails(details);
        return errorInfo;
    }

    /**
     * This class provides a handler for ConstraintViolationException and returns a detailed error response with input validation error codes and messages.
     *
     * @param ex This parameter is of type ConstraintViolationException, which represents the exception thrown when input validation fails.
     * @return ErrorInfo This method returns an ErrorInfo object, which contains a detailed error response with code, message, and attribute details.
     * @author [Author Name]
     * @ResponseStatus This method is annotated with the ResponseStatus annotation, which sets the HTTP status code to BAD_REQUEST (400).
     * @ExceptionHandler This method is annotated with the ExceptionHandler annotation, which specifies that this method will handle ConstraintViolationException.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorInfo handleGlobalValidationException(ConstraintViolationException ex) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(INPUT_VALIDATION_ERROR_CODE);
        errorInfo.setMessage(HttpStatus.BAD_REQUEST.toString());
        List<DetailsTO> details = new ArrayList<>();
        ex.getConstraintViolations().forEach(error -> {
            DetailsTO detailsTO = new DetailsTO();
            detailsTO.setCode(INPUT_VALIDATION_INTERNAL_ERROR_CODE);
            detailsTO.setMessage(INVALID_INPUT_ERROR_MSG);
            AttributeTO attributeTO = new AttributeTO();
            String field = error.getPropertyPath().toString();
            String defaultMessage = error.getMessage();
            String formattedMessage = MessageFormat.format(defaultMessage, field);
            attributeTO.setValue(formattedMessage);
            attributeTO.setKey(field);
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
        return errorMap;
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
        return errorMap;
    }
}
