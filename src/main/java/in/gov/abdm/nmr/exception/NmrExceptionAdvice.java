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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

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

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorDTO> handleNoSuchElementException(HttpServletRequest req, Throwable ex) {
        ErrorDTO error = new ErrorDTO(new Date(), NMRError.NO_SUCH_ELEMENT.getCode(), NMRError.NO_SUCH_ELEMENT.getMessage(), req.getServletPath(), HttpStatus.BAD_REQUEST.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleException(HttpServletRequest req, Throwable ex) {
        LOGGER.error("Unexpected error occurred", ex);
        ErrorDTO error = new ErrorDTO(new Date(), NMRError.NMR_EXCEPTION.getCode(), NMRError.NMR_EXCEPTION.getMessage(), req.getServletPath(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles validation exceptions and returns a customized error response for the client.
     *
     * @param ex The MethodArgumentNotValidException representing the validation error.
     * @return A ResponseEntity containing an ErrorDTO object with error information.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            String formattedMessage = MessageFormat.format(defaultMessage, field);
            errorDTO.setCode(NMRError.INVALID_REQUEST.getCode());
            errorDTO.setMessage(formattedMessage);
            errorDTO.setTimestamp(new Date());
            errorDTO.setHttpStatus(HttpStatus.BAD_REQUEST.toString());
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorDTO, headers, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation exceptions and returns a customized error response for the client.
     *
     * @param ex The MethodArgumentNotValidException representing the validation error.
     * @return A ResponseEntity containing an ErrorDTO object with error information.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleGlobalValidationException(ConstraintViolationException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
            ex.getConstraintViolations().forEach(error -> {
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getMessage();
            String formattedMessage = MessageFormat.format(defaultMessage, field);
            errorDTO.setCode(NMRError.INVALID_REQUEST.getCode());
            errorDTO.setMessage(formattedMessage);
            errorDTO.setTimestamp(new Date());
            errorDTO.setHttpStatus(HttpStatus.BAD_REQUEST.toString());
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorDTO, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DateException.class})
    public ResponseEntity<ErrorDTO> handleDateException(HttpServletRequest req, DateException ex) {
        ErrorDTO error = new ErrorDTO(new Date(), ex.getCode(), ex.getMessage(), req.getServletPath(), ex.getHttpStatus());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<ErrorDTO> handleMaxUploadSizeExceededException(HttpServletRequest req, Throwable ex) {
        ErrorDTO error = new ErrorDTO(new Date(), NMRError.FILE_SIZE_LIMIT.getCode(), NMRError.FILE_SIZE_LIMIT.getMessage(), req.getServletPath(), HttpStatus.FORBIDDEN.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.PAYLOAD_TOO_LARGE.value());
    }

}