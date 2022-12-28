package in.gov.abdm.nmr.api.constant;


import lombok.experimental.UtilityClass;

/**
 * This class holds all the constants associated with NMR application
 */


@UtilityClass
public class NMRConstants {

    public static final String FETCH_COUNT_ON_CARD_URL = "/college/dashboard/cardCount";
    public static final String TOTAL_REGISTRATION_REQUESTS = "Total Registration Requests";
    public static final String TOTAL_UPDATION_REQUESTS = "Total Updation Requests";
    public static final String GENERATE_OTP = "/generateOtp";
    public static final String VALIDATE_OTP = "/validateOtp";
    public static final String OTP_GENERATION_EXCEEDED = "OTP Generation Attempts Exceeded";
    public static final String OTP_ATTEMPTS_EXCEEDED = "OTP Attempts Exceeded";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String EMAIL_BODY = "Your One time password to verify the email address is :";
    public static final String SMS_BODY_PART_ONE = "OTP for creating your ABHA is ";
    public static final String SMS_BODY_PART_TWO = " This One Time Password will be valid for 10 mins ABDM National Health Authority";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String CONTACT_NOT_NULL = "Contact cannot be null or empty";
    public static final String TYPE_NOT_NULL = "Type cannot be null or empty";
    public static final String OTP_NOT_NULL = "OTP cannot be null or empty";
    public static final String SMS_TEMPLATE_ID = "1007164181681962323";
    public static final String EMAIL_TEMPLATE_ID = "1007164181681962323";
    public static final String SUCCESS_RESPONSE = "Success";
    public static final String OTP_INVALID = "Invalid OTP";
    public static final String OTP_NOT_FOUND = "OTP Not Found";
    public static final String OTP_EXPIRED = "OTP Expired";
    public static final String TEMPLATE_ID = "templateId";
    public static final String SUBJECT = "subject";
    public static final String CONTENT = "content";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String MOBILE = "mobile";
    public static final String EMAIL_ID = "emailId";
    public static final int OTP_GENERATION_MAX_ATTEMPTS = 5;
    public static final int OTP_MAX_ATTEMPTS = 3;

    public static final String TOTAL_SUSPENSION_REQUESTS="Total Suspension Requests";

    public static final String TOTAL_BLACK_LIST_REQUESTS="Total BlackList Requests";

    public static final String TOTAL_VOLUNTARY_RETIREMENT_REQUESTS="Total Voluntary Retirement Requests";

    public static final String HP_PROFILE_ID="hp_profile_id";

    public static final String ID="id";

    public static final String HP_PROFILE_STATUS_ID="hp_profile_status_id";

    public static final String APPLICATION_STATUS_TYPE_ID="application_status_type_id";

    public static final String VERIFIED_BY="verified_by";

    public static final String SCHEDULE_ID="schedule_id";

    public static final String USER_TYPE_ID="user_type_id";

    public static final String USER_TYPE="userType";

    public static final String USER_SUB_TYPE="userSubType";

    public static final String USER_SUB_TYPE_ID="user_sub_type_id";

    public static final String APPLICATION_STATUS_TYPE="applicationStatusType";

    public static final String FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY="SELECT hvs.hpProfile.hpProfileStatus.name as name, COUNT(hvs) as count " +
            "FROM HpVerificationStatus hvs " +
            "WHERE hvs.applicationStatusType.name=:applicationStatusType " +
            "AND hvs.verifiedBy.userType.name=:userType " +
            "GROUP BY hvs.hpProfile.hpProfileStatus.name";

    public static final String FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY="SELECT hvs.id from HpVerificationStatus hvs " +
            "WHERE hvs.applicationStatusType.name=:applicationStatusType " +
            "AND hvs.verifiedBy.userType.name=:userType ";

    public static final String FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY="SELECT hvs.hpProfile.hpProfileStatus.name as name, COUNT(hvs) as count " +
            "FROM HpVerificationStatus hvs " +
            "WHERE hvs.applicationStatusType.name=:applicationStatusType " +
            "AND hvs.verifiedBy.userType.name=:userType " +
            "AND hvs.verifiedBy.userSubType.name=:userSubType " +
            "GROUP BY hvs.hpProfile.hpProfileStatus.name";

    public static final String FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY="SELECT hvs.id from HpVerificationStatus hvs " +
            "WHERE hvs.applicationStatusType.name=:applicationStatusType " +
            "AND hvs.verifiedBy.userType.name=:userType " +
            "AND hvs.verifiedBy.userSubType.name=:userSubType ";

    public static final String REGISTRATION="Registration";

    public static final String UPDATION="Updation";

    public static final String SUSPENSION="Suspension";

    public static final String BLACK_LIST="Blacklist";

    public static final String VOLUNTARY_RETIREMENT="Voluntary Retirement";

    public static final String INVALID_USER_SUB_TYPE="Invalid User sub-type. Expected: College, College Dean or College Registrar";

    public static final String INVALID_USER_TYPE="Invalid User type. Expected: Health Professional, College, State Medical Council or National Medical Council";



}
