package in.gov.abdm.nmr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NMRError {
    INVALID_REQUEST("ABDM-NMR-001", "Invalid request. "),
    NMR_EXCEPTION("ABDM-NMR-002", "Nmr exception"),
    NO_DATA_FOUND("ABDM-NMR-003", "No data found"),
    OTP_INVALID("ABDM-NMR-004", "The OTP you entered is invalid. Please double check the code you received and try again."),
    OTP_ATTEMPTS_EXCEEDED("ABDM-NMR-005", "You have exceeded the maximum number of attempts to enter the OTP. For security reason, your account has been temporarily locked.Please wait for 30 minutes before attempting again."),
    OTP_EXPIRED("ABDM-NMR-006", "The OTP you have entered has expired. Please try again by requesting a new OTP."),
    TEMPLATE_NOT_FOUND("ABDM-NMR-007", "Template not found"),
    WORK_FLOW_EXCEPTION("ABDM-NMR-008", "Failed to process work flow"),
    ACCESS_DENIED_EXCEPTION("ABDM-NMR-009", "Unauthorized Access"),
    NO_SUCH_ELEMENT("ABDM-NMR-010", "There is no matching value for the given request parameter"),
    INVALID_ID_EXCEPTION("ABDM-NMR-011", "Invalid ID"),
    DATE_EXCEPTION("ABDM-NMR-012", "Invalid date format"),
    RESOURCE_EXISTS_EXCEPTION("ABDM-NMR-013", "Resource already exists"),
    NOT_FOUND_EXCEPTION("ABDM-NMR-014", "Resource not found"),
    WORK_FLOW_CREATION_FAIL("ABDM-NMR-015", "We process only one request at a time. Please wait until your current request has been completed before submitting another. You can track current application status via Track Application"),
    INVALID_USER("ABDM-NMR-016", "In-valid user"),
    PROFILE_NOT_APPROVED("ABDM-NMR-017", "Only Approved profile can be suspended"),
    PROFILE_NOT_SUSPEND("ABDM-NMR-018", "Only Suspended profile can be reactivated"),
    INVALID_SEARCH_CRITERIA("ABDM-NMR-019", "Invalid search criteria"),
    MISSING_SEARCH_VALUE("ABDM-NMR-020", "Please Enter a search value"),
    ACCESS_FORBIDDEN("ABDM-NMR-021", "Access Forbidden"),
    USE_LATEST_HP_ID("ABDM-NMR-022", "Please use the latest ongoing HP Profile id for updation."),
    MISSING_MANDATORY_FIELD("ABDM-NMR-023", "Mandatory field is missing"),
    CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS("ABDM-NMR-024", "Please choose a new password that is different from your previous five passwords."),
    ID_NOT_EXIST("ABDM-NMR-025", "Id does not exist"),
    PROOFS_EMPTY_ERROR("ABDM-NMR-026", "Please provide at-least one proof document"),
    MISSING_PROOFS_ERROR("ABDM-NMR-027", "Please ensure the proofs for all declared qualifications has been provided"),
    EXCESS_PROOFS_ERROR("ABDM-NMR-028", "Please ensure the proofs for only declared qualifications has been provided"),
    QUALIFICATION_DETAILS_LIMIT_EXCEEDED("ABDM-NMR-029", "Kindly limit your submission to a maximum of seven additional qualification."),
    MOBILE_NUM_ALREADY_REGISTERED("ABDM-NMR-030", "The mobile number is already registered. Please ensure that you have entered the correct mobile number or try logging in with associated account."),
    EMAIL_ID_ALREADY_REGISTERED("ABDM-NMR-031", "The email address is already registered. Please ensure that you have entered the correct mobile number or try logging in with associated account."),
    USERNAME_ALREADY_REGISTERED("ABDM-NMR-032", "The username that you entered is already registered. Please choose a different username that is unique and not currently in use."),
    STATE_MEDICAL_ID_NULL("ABDM-NMR-033", "State medical council id cannot be null for creating SMC user"),
    INVALID_USER_TYPE("ABDM-NMR-034", "User type or sub type invalid."),
    NON_REGISTERED_MOBILE_NUMBER("ABDM-NMR-035", "The mobile provided is not registered in our system. Please ensure that you have entered the correct mobile number or consider using a registered mobile number for receiving the OTP."),
    NON_REGISTERED_EMAIL_ID("ABDM-NMR-036", "The email address provided is not registered in our system. Please ensure that you have entered the correct email address or consider using a registered email address for receiving the OTP."),
    NON_REGISTERED_NMR_ID("ABDM-NMR-037", "The NMR ID provided is not registered in our system. Please ensure that you have entered the correct NMR ID or consider using a registered NMR ID for receiving the OTP."),
    INVALID_OTP_TRANSACTION_ID("ABDM-NMR-038", "Invalid OTP Transaction ID"),
    TEMPLATE_KEYS_MISSING("ABDM-NMR-039", "Error while retrieving template keys");
    private final String code;
    private final String message;
}
