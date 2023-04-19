package in.gov.abdm.nmr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    DATE_EXCEPTION("ABDM-NMR-015", "Invalid date format"),
    ALREADY_EXIST_EXCEPTION("ABDM-NMR-016", "Resource Already Exists"),
    NOT_FOUND_EXCEPTION("ABDM-NMR-017", "Resource Not Found"),
    WORK_FLOW_CREATION_FAIL("ABDM-NMR-018", "Cant create new request until an existing request is closed."),
    CANNOT_RAISE_REQUEST("ABDM-NMR-019", "You cannot raise additional qualification request until NMR Id is generated."),
    CURRENT_WORK_DETAILS_MANDATORY("ABDM-NMR-020", "The field 'currentWorkDetails' is mandatory."),
    IN_VAVALID_USER("ABDM-NMR-021", "In-Valid User."),
    FAIL_ELASTIC_UPDATE("ABDM-NMR-022", "Exception while indexing HP."),
    PROFILE_NOT_APPROVED("ABDM-NMR-023", "Approved profile can only be suspended."),
    PROFILE_NOT_SUSPEND("ABDM-NMR-024", "Suspended profile can only be reactivated."),
    INVALID_SEARCH_CRITERIA_FOR_REACTIVATE_LICENSE("ABDM-NMR-025", "Invalid Search Criteria. Expected: search."),
    MISSING_SEARCH_VALUE("ABDM-NMR-026", "Please Enter a search value."),
    INVALID_SEARCH_CRITERIA_FOR_TRACK_STATUS_AND_APPLICATION("ABDM-NMR-027", "Invalid Search Criteria. Expected: registrationnumber or smcid"),
    ACCESS_FORBIDDEN("ABDM-NMR-028", "Access Forbidden"),
    INVALID_GROUP("ABDM-NMR-029", "Invalid Group. Expected: Health Professional, State Medical Council, National Medical Council, College Dean, College Registrar or College Admin"),
    INVALID_APPLICATION_TYPE("ABDM-NMR-030", "Invalid Application Type. Expected: HP Registration, HP Modification, Temporary Suspension, Permanent Suspension, Activate License, College Registration"),
    INVALID_WORK_FLOW_STATUS("ABDM-NMR-031", "Invalid Workflow Status. Expected: Pending, Approved, Query Raised, Rejected, Suspended or Blacklisted"),
    INVALID_SEARCH_CRITERIA_FOR_GET_CARD_DETAIL("ABDM-NMR-032", "Invalid Search Criteria. Expected: name, nmrid, smcid or search"),
    INVALID_SEARCH_CRITERIA_FOR_POST_CARD_DETAIL("ABDM-NMR-033", "Invalid Search Criteria. Expected: collegeid, name, nmrid, smcid, workflowstatus or search"),
    USE_LATEST_HP_ID("ABDM-NMR-034", "Please use the latest ongoing HP Profile id for updation."),
    NO_MATCHING_USER_DETAILS_FOUND("ABDM-NMR-035", "No matching User details found for the given hp_profile_id."),
    NO_MATCHING_WORK_PROFILE_DETAILS_FOUND("ABDM-NMR-036", "No matching work profile details found for the given hp_profile_id."),
    MISSING_TRANSACTION_ID_ERROR("ABDM-NMR-037", "The field Transaction id is mandatory."),
    INVALID_DATE_FORMAT("ABDM-NMR-038", "Invalid date format"),
    CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS("ABDM-NMR-039", "Current password should not be same as last 5 passwords"),
    APPLICATION_TYPE_ID_NOT_EXIST("ABDM-NMR-040", "Application Type Id do not exists"),
    INVALID_PROFILE_STATUS_CODE("ABDM-NMR-041", "Invalid profile status code"),
    FAIL_TO_SEARCH_HP("ABDM-NMR-042", "Exception while searching for HP"),
    FAIL_TO_RETRIEVE_HP("ABDM-NMR-043", "Exception while retrieving HP profile"),
    INVALID_COLLEGE_ID("ABDM-NMR-044", "Invalid college id"),
    INVALID_PROFILE_ID("ABDM-NMR-045", "Invalid profile id"),
    NEXT_GROUP_NOT_FOUND("ABDM-NMR-046", "Next Group Not Found"),
    COLLEGE_VERIFIER_FOUND("ABDM-NMR-047", "No college verifier found for id"),
    WORK_PROFILE_DETAILS_NULL_ERROR("ABDM-NMR-048", "The field 'currentWorkDetails' is mandatory"),
    PROOFS_EMPTY_ERROR("ABDM-NMR-049", "Please provide at-least one proof document"),
    MISSING_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR("ABDM-NMR-050", "Please provide proofs for all the work profile details provided"),
    EXCESS_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR("ABDM-NMR-051", "Please remove excess proofs to sync with the work profile details provided"),
    WORK_PROFILE_DETAILS_LIMIT_EXCEEDED("ABDM-NMR-052", "Please provide less than or equal to 6 work profiles at a time."),
    QUALIFICATION_DETAILS_NULL_ERROR("ABDM-NMR-053", "The field 'qualificationDetails' is mandatory."),
    QUALIFICATION_DETAILS_EMPTY_ERROR("ABDM-NMR-054", "Please provide at-least one qualification to be added."),
    PROOFS_NULL_ERROR("ABDM-NMR-055", "The field 'proofs' is mandatory."),
    MISSING_PROOFS_ERROR("ABDM-NMR-056", "Please provide proofs for all the qualification details provided."),
    EXCESS_PROOFS_ERROR("ABDM-NMR-057", "Please remove excess proofs to sync with the qualification details provided."),
    QUALIFICATION_DETAILS_LIMIT_EXCEEDED("ABDM-NMR-058", "Please provide less than or equal to 6 qualifications at a time."),
    MOBILE_NUM_ALREADY_REGISTERED("ABDM-NMR-059", "Mobile number already registered."),
    EMAIL_NUM_ALREADY_REGISTERED("ABDM-NMR-059", "Email id already registered."),


    ;
    private final String code;
    private final String message;
}
