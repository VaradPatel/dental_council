package in.gov.abdm.nmr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NMRError {

    DATABASE_UNAVAILABLE("NMR-1000: ", "Unable to connect the database"),
    DATA_NOT_FOUND("NMR-1001: ", "No data found"),
    EXCEEDED_MULTIPLE_OTP_REQUEST_OR_OTP_MATCH("NMR-1002: ", "You have requested multiple OTPs Or Exceeded maximum number of attempts for OTP match in this transaction. Please try again in 10 minutes."),
    OTP_ATTEMPTS_EXCEEDED("NMR-1003", "You have requested multiple OTPs Or Exceeded maximum number of attempts for OTP match in this transaction. Please try again in 30 minutes."),
    INVALID_REQUEST("NMR-1004: ", "Invalid request"),
    OTP_EXPIRED("NMR-1005", "OTP Expired Or Not Found"),
    OTP_INVALID("NMR-1006", "invalid OTP"),

    TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES ("NMR-1007","Template id not found in properties"),
    TEMPLATE_NOT_FOUND("1008", "Template not found"),
    NO_DATA_FOUND("1009", "No data found"),
    DATE_EXCEPTION("10010", "Fail to Process"),


    ;

    private final String code;
    private final String message;
}
