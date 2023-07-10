package in.gov.abdm.nmr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NMRError {
    INVALID_REQUEST("ABDM-NMR-001", "Invalid request"),
    NMR_EXCEPTION("ABDM-NMR-002", "Nmr exception"),
    NO_DATA_FOUND("ABDM-NMR-003", "No data found"),
    OTP_INVALID("ABDM-NMR-004", "Invalid OTP"),
    OTP_ATTEMPTS_EXCEEDED("ABDM-NMR-005", "Exceeded maximum number of attempts for OTP in this transaction. Please try again in 30 minutes"),
    OTP_EXPIRED("ABDM-NMR-006", "OTP Expired"),
    TEMPLATE_NOT_FOUND("ABDM-NMR-007", "Template not found"),
    WORK_FLOW_EXCEPTION("ABDM-NMR-008", "Failed to process work flow"),
    ACCESS_DENIED_EXCEPTION("ABDM-NMR-009", "Unauthorized Access"),
    NO_SUCH_ELEMENT("ABDM-NMR-010", "There is no matching value for the given request parameter"),
    INVALID_ID_EXCEPTION("ABDM-NMR-011", "Invalid ID"),
    DATE_EXCEPTION("ABDM-NMR-012", "Invalid date format"),
    RESOURCE_EXISTS_EXCEPTION("ABDM-NMR-013", "Resource already exists"),
    NOT_FOUND_EXCEPTION("ABDM-NMR-014", "Resource not found"),
    WORK_FLOW_CREATION_FAIL("ABDM-NMR-015", "We are sorry, but we can only process one request at a time. Please wait until your current request has been completed before submitting another. You can track current application status via track Application"),
    INVALID_USER("ABDM-NMR-016", "In-valid user"),
    PROFILE_NOT_APPROVED("ABDM-NMR-017", "Only Approved profile can be suspended"),
    PROFILE_NOT_SUSPEND("ABDM-NMR-018", "Only Suspended profile can be reactivated"),
    INVALID_SEARCH_CRITERIA("ABDM-NMR-019", "Invalid search criteria"),
    MISSING_SEARCH_VALUE("ABDM-NMR-020", "Please Enter a search value"),
    ACCESS_FORBIDDEN("ABDM-NMR-021", "Access Forbidden"),
    USE_LATEST_HP_ID("ABDM-NMR-022", "Please use the latest ongoing HP Profile id for updation."),
    MISSING_MANDATORY_FIELD("ABDM-NMR-023", "Mandatory field is missing"),
    CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS("ABDM-NMR-024", "Current password should not be same as last 5 passwords"),
    ID_NOT_EXIST("ABDM-NMR-025", "Id does not exist"),
    PROOFS_EMPTY_ERROR("ABDM-NMR-026", "Please provide at-least one proof document"),
    MISSING_PROOFS_ERROR("ABDM-NMR-027", "Please provide proofs for all the qualification details provided"),
    EXCESS_PROOFS_ERROR("ABDM-NMR-028", "Please remove excess proofs to sync with the qualification details provided"),
    QUALIFICATION_DETAILS_LIMIT_EXCEEDED("ABDM-NMR-029", "Kindly limit your submission to a maximum of seven additional qualification."),
    MOBILE_NUM_ALREADY_REGISTERED("ABDM-NMR-030", "Mobile number already registered"),
    EMAIL_ID_ALREADY_REGISTERED("ABDM-NMR-031", "Email id already registered"),
    USERNAME_ALREADY_REGISTERED("ABDM-NMR-032", "Username already registered"),
    STATE_MEDICAL_ID_NULL("ABDM-NMR-033", "State medical council id cannot be null for creating SMC user"),
    INVALID_USER_TYPE("ABDM-NMR-034", "User type or sub type invalid."),
    NON_REGISTERED_MOBILE_NUMBER("ABDM-NMR-035", "This mobile number is not registered in NMR."),
    NON_REGISTERED_EMAIL_ID("ABDM-NMR-036", "This email ID is not registered in NMR."),
    NON_REGISTERED_NMR_ID("ABDM-NMR-037", "This NMR ID is not registered in NMR."),
    INVALID_OTP_TRANSACTION_ID("ABDM-NMR-038", "Invalid OTP Transaction ID"),
    TEMPLATE_KEYS_MISSING("ABDM-NMR-039", "Error while retrieving template keys"),
    DUPLICATE_QUALIFICATION_ERROR("ABDM-NMR-040", "Duplicate qualification details detected. Please remove the duplicate entry.");
    private final String code;
    private final String message;
}
