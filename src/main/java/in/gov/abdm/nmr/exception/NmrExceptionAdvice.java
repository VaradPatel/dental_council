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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * Handles TemplateException and returns a JSON response entity with HTTP status code 400.
     *
     * @param ex  the TemplateException object containing the error details
     * @param req the HttpServletRequest object representing the current request
     * @return a ResponseEntity object containing an ErrorDTO object and HTTP status code 400
     */
    @ExceptionHandler(value = {WorkFlowException.class, InvalidRequestException.class, NmrException.class,
            OtpException.class, TemplateException.class, NoDataFoundException.class, ResourceExistsException.class, InvalidIdException.class})
    public ResponseEntity<ErrorDTO> handleBadRequest(ABDMBaseException ex, HttpServletRequest req) {
        ErrorDTO error = new ErrorDTO(new Date(), ex.getCode(), ex.getMessage(), req.getServletPath(), ex.getHttpStatus());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> handleSecurityException(HttpServletRequest req, Throwable ex) {
        ErrorDTO error = new ErrorDTO(new Date(), NMRError.ACCESS_DENIED_EXCEPTION.getCode(), NMRError.ACCESS_DENIED_EXCEPTION.getMessage(), req.getServletPath(), HttpStatus.FORBIDDEN.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleException(HttpServletRequest req, Throwable ex) {
        LOGGER.error("Unexpected error occured", ex);
        ErrorDTO error = new ErrorDTO(new Date(), NMRError.INTERNAL_SERVER_ERROR.getCode(), NMRError.INTERNAL_SERVER_ERROR.getMessage(), req.getServletPath(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
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
        errorInfo.setCode(NMRError.INPUT_VALIDATION_ERROR_CODE.getCode());
        errorInfo.setMessage(HttpStatus.BAD_REQUEST.toString());
        List<DetailsTO> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            DetailsTO detailsTO = new DetailsTO();
            detailsTO.setCode(NMRError.INPUT_VALIDATION_INTERNAL_ERROR_CODE.getCode());
            detailsTO.setMessage(NMRError.INPUT_VALIDATION_INTERNAL_ERROR_CODE.getMessage());
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
        errorInfo.setCode(NMRError.INPUT_VALIDATION_ERROR_CODE.getCode());
        errorInfo.setMessage(HttpStatus.BAD_REQUEST.toString());
        List<DetailsTO> details = new ArrayList<>();
        ex.getConstraintViolations().forEach(error -> {
            DetailsTO detailsTO = new DetailsTO();
            detailsTO.setCode(NMRError.INPUT_VALIDATION_INTERNAL_ERROR_CODE.getCode());
            detailsTO.setMessage(NMRError.INPUT_VALIDATION_INTERNAL_ERROR_CODE.getMessage());
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

    @ExceptionHandler({DateException.class})
    public ResponseEntity<ErrorDTO> handleDateException(HttpServletRequest req, DateException ex) {
        ErrorDTO error = new ErrorDTO(new Date(), ex.getCode(), ex.getMessage(), req.getServletPath(), ex.getHttpStatus());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}