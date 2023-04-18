package in.gov.abdm.nmr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NMRError {
    INVALID_REQUEST("ABDM-NMR-001", "Invalid request"),
    NMR_EXCEPTION("ABDM-NMR-002", "nmr exception"),
    NO_DATA_FOUND("ABDM-NMR-003", "No data found"),
    OTP_INVALID("ABDM-NMR-004", "invalid OTP"),
    OTP_ATTEMPTS_EXCEEDED("ABDM-NMR-005", "You have requested multiple OTPs Or Exceeded maximum number of attempts for OTP match in this transaction. Please try again in 30 minutes."),
    OTP_EXPIRED("ABDM-NMR-006", "OTP Expired Or Not Found"),
    TEMPLATE_NOT_FOUND("ABDM-NMR-007", "Template not found"),
    TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES("ABDM-NMR-008", "Template id not found in properties"),
    WORK_FLOW_EXCEPTION("ABDM-NMR-009", "Fail to Process work flow"),
    ACCESS_DENIED_EXCEPTION("ABDM-NMR-010", "Fail to Process work flow"),
    INTERNAL_SERVER_ERROR("ABDM-NMR-011", "Internal Server Error"),
    INPUT_VALIDATION_ERROR_CODE("ABDM-NMR-012", "Invalid input"),
    INPUT_VALIDATION_INTERNAL_ERROR_CODE("ABDM-NMR-013", "Invalid input"),
    INVALID_ID_EXCEPTION("ABDM-NMR-014", "invalid ID"),
    DATE_EXCEPTION("ABDM-NMR-015", "Fail to Process"),
    ALREADY_EXIST_EXCEPTION("ABDM-NMR-016", "Resource Already Exists"),
    NOT_FOUND_EXCEPTION("ABDM-NMR-017", "Resource Not Found"),

    ;

    private final String code;
    private final String message;
}
