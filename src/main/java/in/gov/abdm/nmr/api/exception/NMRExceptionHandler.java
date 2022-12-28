package in.gov.abdm.nmr.api.exception;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This RestControllerAdvice handles exceptions and error codes while validating input request fields
 */

@RestControllerAdvice
@Slf4j
public class NMRExceptionHandler {

    /**
     * Constant for logging
     */
    private static final String CONTROLLER_ADVICE_EXCEPTION_CLASS = "WebExchangeBindException :{}";

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

    /**
     * <p>
     * MethodArgumentNotValidException exception is thrown after data binding
     * and validation failure.
     * This method returns BindingResult from MethodArgumentNotValidException and then
     * compose list of error messages based on all rejected fields
     * </p>
     *
     * @param ex
     * @return list of error messages after analysis of binding and validation errors.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
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
        System.out.println(e);
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put(RESPONSE_TIMESTAMP, LocalDate.now());
        log.error(e.getMessage());
        errorMap.put(MESSAGE, e.getMessage());
        return errorMap ;
    }
}
