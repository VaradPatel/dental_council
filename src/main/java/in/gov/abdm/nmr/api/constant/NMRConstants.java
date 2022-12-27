package in.gov.abdm.nmr.api.constant;


import lombok.experimental.UtilityClass;

/**
 * This class holds all the constants associated with NMR application
 */


@UtilityClass
public class NMRConstants {

    public static final String FETCH_COUNT_ON_CARD_URL = "/college/dashboard/cardCount";
    public static final String GENERATE_OTP = "/generateOtp";
    public static final String GENERATE_AADHAR_OTP = "/sendAadhaarOtp";
    public static final String VALIDATE_OTP = "/validateOtp";
    public static final String VALIDATE_AADHAR_OTP = "/verifyAadhaarOtp";
    public static final String AADHAR_SERVICE_SEND_OTP = "/api/v3/aadhaar/sendOtp";
    public static final String AADHAR_SERVICE_VERIFY_OTP = "/api/v3/aadhaar/verifyOtp";
    public static final String NOTIFICATION_SERVICE_SEND_MESSAGE = "/internal/v3/notification/message";
    public static final String IS_NEW = "isNew";
    public static final String TOTAL_REGISTRATION_REQUESTS = "Total Registration Requests";
    public static final String TOTAL_UPDATION_REQUESTS = "Total Updation Requests";
    public static final String OTP_GENERATION_EXCEEDED = "OTP Generation Attempts Exceeded";
    public static final String OTP_ATTEMPTS_EXCEEDED = "OTP Attempts Exceeded";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String EMAIL_BODY = "Your One time password to verify the email address is :";
    public static final String SMS_BODY_PART_ONE = "OTP for creating your ABHA is ";
    public static final String SMS_BODY_PART_TWO = " This One Time Password will be valid for 10 mins ABDM National Health Authority";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String CONTACT_NOT_NULL = "Contact cannot be null or empty";
    public static final String AADHAR_NOT_NULL = "AADHAR Number cannot be null or empty";
    public static final String TYPE_NOT_NULL = "Type cannot be null or empty";
    public static final String TRANSACTION_ID_NOT_NULL = "Transaction Id cannot be null or empty";
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
}
