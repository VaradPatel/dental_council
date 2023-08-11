package in.gov.abdm.nmr.exception;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NMRError {
    INVALID_REQUEST("ABDM-NMR-001", "An error occurred while processing your request. Please validate the input parameters and try again."),
    NMR_EXCEPTION("ABDM-NMR-002", "An error occurred while processing your request. Please try again in sometime."),
    NO_DATA_FOUND("ABDM-NMR-003", "The requested data could not be located. Please verify the input and try again."),
    OTP_INVALID("ABDM-NMR-004", "The OTP you entered is invalid. Please double check the code you received and try again."),
    OTP_ATTEMPTS_EXCEEDED("ABDM-NMR-005", "You have exceeded the maximum number of attempts to enter the OTP. For security reason, your account has been temporarily locked for 30 minutes."),
    OTP_EXPIRED("ABDM-NMR-006", "The OTP you have entered has expired. Please try again by requesting a new OTP."),
    TEMPLATE_NOT_FOUND("ABDM-NMR-007", "The requested template could not be located. Please verify the input and try again."),
    WORK_FLOW_EXCEPTION("ABDM-NMR-008", "An error occurred while processing your request. Please try again later in sometime."),
    ACCESS_DENIED_EXCEPTION("ABDM-NMR-009", "Forbidden. You do not have permissions to access this resource. Please contact the administrator for assistance."),
    NO_SUCH_ELEMENT("ABDM-NMR-010", "There is no matching value for the given request parameter."),
    INVALID_ID_EXCEPTION("ABDM-NMR-011", "Invalid ID. The provided ID could not be located. Please verify the ID and try again."),
    DATE_EXCEPTION("ABDM-NMR-012", "Invalid date format. Please enter the date in correct format ("+ NMRConstants.DATE_FORMAT+") and try again."),
    RESOURCE_EXISTS_EXCEPTION("ABDM-NMR-013", "Resource already exists. Please provide a unique identifier or modify the existing resource."),
    NOT_FOUND_EXCEPTION("ABDM-NMR-014", "The requested resource could not be located. Please verify the input and try again."),
    WORK_FLOW_CREATION_FAIL("ABDM-NMR-015", "We can process only one request at a time"),
    PROFILE_NOT_APPROVED("ABDM-NMR-016", "Only approved profile can be suspended."),
    PROFILE_NOT_SUSPEND("ABDM-NMR-017", "User profile is already activated"),
    INVALID_SEARCH_CRITERIA("ABDM-NMR-018", "An error occurred while processing your request. Please verify the search parameters and try again."),
    MISSING_SEARCH_VALUE("ABDM-NMR-019", "An error occurred while processing your request. Please specify the value to be used for filtering the records."),
    MISSING_MANDATORY_FIELD("ABDM-NMR-020", "Missing mandatory fields. Please ensure all required fields are provided as per API documentation."),
    CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS("ABDM-NMR-021", "Please choose a new password that is different from your previous five passwords."),
    MISSING_PROOFS_ERROR("ABDM-NMR-022", "Please ensure the proofs for all declared qualifications has been provided."),
    EXCESS_PROOFS_ERROR("ABDM-NMR-023", "Please ensure the proofs for only declared qualifications has been provided."),
    QUALIFICATION_DETAILS_LIMIT_EXCEEDED("ABDM-NMR-024", "Kindly limit your submission to a maximum of seven additional qualification."),
    MOBILE_NUM_ALREADY_REGISTERED("ABDM-NMR-025", "The mobile number is already registered"),
    EMAIL_ID_ALREADY_REGISTERED("ABDM-NMR-026", "The email address is already registered"),
    USERNAME_ALREADY_REGISTERED("ABDM-NMR-027", "The username that you entered is already registered"),
    STATE_MEDICAL_ID_NULL("ABDM-NMR-028", "An error occurred while processing your request. Please specify the state medical council and try again."),
    INVALID_USER_TYPE("ABDM-NMR-029", "An error occurred while processing your request. Please validate the user type in request and try again."),
    NON_REGISTERED_MOBILE_NUMBER("ABDM-NMR-030", "The mobile number provided is not registered in our system"),
    NON_REGISTERED_EMAIL_ID("ABDM-NMR-031", "The email address provided is not registered in our system"),
    NON_REGISTERED_NMR_ID("ABDM-NMR-032", "The NMR ID provided is not registered in our system"),
    INVALID_OTP_TRANSACTION_ID("ABDM-NMR-033", "An error occurred while processing your request. Please ensure that a correct transaction id is used for validating OTP."),
    TEMPLATE_KEYS_MISSING("ABDM-NMR-034", "An error occurred while accessing the template keys. Please try again in sometime."),
    FILE_SIZE_LIMIT("ABDM-NMR-035", "Kindly limit the file size to allowable limit of 5MB"),
    FORWARD_ACTION_NOT_ALLOWED("ABDM-NMR-036", "Forward action is not supported for international qualification verification."),
    EMAIL_ATTEMPTS_EXCEEDED("ABDM-NMR-037", "You have exceeded the maximum number of attempts to verify email. Please try again after 30 minutes.");
    private final String code;
    private final String message;
}
