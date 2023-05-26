package in.gov.abdm.nmr.util;


import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

/**
 * This class holds all the constants associated with NMR application
 */
@UtilityClass
public class NMRConstants {
    public static final String NOTIFICATION_REQUEST_MAPPING = "/notification";
    public static final String SEND_OTP = "/send-otp";
    public static final String VERIFY_OTP = "/verify-otp";
    public static final String RETRIEVE_USER = "/retrieve-user";
    public static final String FACILITY_SERVICE_SEARCH = "/v1.5/facility/search";
    public static final String NOTIFICATION_SERVICE_SEND_MESSAGE = "/internal/v3/notification/message";
    public static final String NOTIFICATION_DB_SERVICE_GET_TEMPLATE = "/internal/v3/notification/template/id/{id}";
    public static final String RESET_PASSWORD = "/user/reset-password";
    public static final String RAISE_QUERY = "/health-professional/queries";
    public static final String KYC_FUZZY_MATCH = "/health-professional/{registrationNumber}/kyc";
    public static final String GET_QUERIES = "health-professional/{healthProfessionalId}/queries";
    public static final String VERIFY_EMAIL = "/user/verify-email";
    public static final String SET_PASSWORD = "/user/set-password";
    public static final String E_SIGN = "/e-signature";
    public static final String PATH_FACILITY_ROOT = "/facilities";
    public static final String PATH_FACILITY_SEARCH = "/search";
    public static final String APPLICATION_REQUEST_URL = "/health-professional/applications";
    public static final String HEALTH_PROFESSIONAL_ACTION = APPLICATION_REQUEST_URL + "/status";
    public static final String COLLEGES_ACTION = "/college/applications/status";
    public static final String APPLICATION_DETAILS = "/applications/{requestId}";
    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String FACILITY_SERVICE = "facility";
    public static final String NOTIFICATION_DB_SERVICE = "notification-db";
    public static final String LGD_DB_SERVICE = "lgd-db";
    public static final String QUERY_CLOSED_STATUS = "closed";
    public static final String QUERY_OPEN_STATUS = "open";
    public static final String GLOBAL_NOTIFICATION_ENDPOINT = "${global.notification.endpoint}";
    public static final String GLOBAL_LGD_ENDPOINT = "${global.lgd.endpoint}";
    public static final String LGD_API_VERSION = "/internal/v3/abdm";
    public static final String LGD = "/lgd";
    public static final String LGD_SEARCH_URL = LGD + "/search";
    public static final String LGD_FEIGN_SEARCH_URL = LGD_API_VERSION + LGD_SEARCH_URL;
    public static final String PIN_CODE = "pinCode";
    public static final String RENEWABLE_REGISTRATION_CODE = "1";
    public static final String VIEW = "view";
    public static final String GLOBAL_FACILITY_ENDPOINT = "${global.facility.endpoint}";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String OTP_EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String INFO_EMAIL_SUBJECT = "NMR : Status Changed";
    public static final String INFO_EMAIL_VERIFICATION_SUCCESSFUL_SUBJECT = "NMR : Verification Successful";
    public static final String INFO_EMAIL_SET_PASSWORD_SUBJECT = "NMR : Set New Password";
    public static final String ACCOUNT_CREATED_SUBJECT = "Account Created";
    public static final String VERIFY_EMAIL_SUBJECT = "NMR: Verify Email";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String SMS_OTP_MESSAGE_PROPERTIES_KEY = "sms-otp";
    public static final String EMAIL_OTP_MESSAGE_PROPERTIES_KEY = "email-otp";
    public static final String SMS_VERIFIED_MESSAGE_PROPERTIES_KEY = "sms-verified";
    public static final String EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY = "email-verified";
    public static final String STATUS_CHANGED_MESSAGE_PROPERTIES_KEY = "status-changed";
    public static final String INFO_CONTENT_TYPE = "info";
    public static final String CONTACT_NOT_NULL = "Contact cannot be null or empty";
    public static final String OTP_NOT_NULL = "OTP cannot be null or empty";

    public static final String SUCCESS_RESPONSE = "Success";
    public static final String FAILURE_RESPONSE = "Fail";
    public static final String SENT_RESPONSE = "sent";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_VERIFIED = "This email is already verified";
    public static final String EMAIL_USED_BY_OTHER_USER = "Email is already used by other user";
    public static final String COLLEGE_CONSTANT = "COLLEGE";
    public static final String USERNAME_ALREADY_EXISTS = "User with this username already exists";
    public static final String MOBILE_NUMBER_ALREADY_EXISTS = "User with this mobile number already exists";
    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INVALID_COLLEGE_ID = "Invalid college id";
    public static final String INVALID_PROFILE_ID = "Invalid profile id";
    public static final String LINK_EXPIRED = "Link expired";
    public static final String OLD_PASSWORD_NOT_MATCHING = "Old password is not correct";
    public static final String PROBLEM_OCCURRED = "Problem Occurred";
    public static final String USERNAME_NOT_NULL = "Username cannot be null or empty";
    public static final String PASSWORD_NOT_NULL = "Password cannot be null or empty";
    public static final String REGISTRATION_NOT_NULL = "Registration number cannot be null or empty";
    public static final String SMC_NOT_NULL = "SMC ID number cannot be null or empty";
    public static final String TOKEN_NOT_NULL = "Token cannot be null or empty";
    public static final int RESET_PASSWORD_LINK_EXPIRY_HOURS = 24;
    public static final String TEMPLATE_ID = "templateId";
    public static final String SUBJECT = "subject";

    public static final String USER_ID = "userId";
    public static final String CONTENT = "content";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String MOBILE = "mobile";
    public static final String EMAIL_ID = "emailId";
    public static final String SMS = "sms";
    public static final String EMAIL = "email";
    public static final int OTP_GENERATION_MAX_ATTEMPTS = 5;
    public static final int OTP_MAX_ATTEMPTS = 3;
    public static final String TOTAL = "Total";
    public static final String TEMPORARY_SUSPENSION_APPLICATION_TYPE_ID = "3";
    public static final String PERMANENT_SUSPENSION_APPLICATION_TYPE_ID = "4";
    public static final String TEMPORARY_AND_PERMANENT_SUSPENSION_APPLICATION_TYPE_ID = "3,4";

    public static final String PENDING = "Pending";

    public static final String APPROVED = "Approved";
    public static final String TOTAL_HP_REGISTRATION_REQUESTS = "Total Registration Requests";
    public static final String TOTAL_HP_MODIFICATION_REQUESTS = "Total Modification Requests";
    public static final String TOTAL_TEMPORARY_SUSPENSION_REQUESTS = "Total Temporary Suspension Requests";
    public static final String TOTAL_PERMANENT_SUSPENSION_REQUESTS = "Total Permanent Suspension Requests";
    public static final String TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS = "Total Suspension Requests";
    public static final String CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS = "Temporary Suspension Requests Received";
    public static final String CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS = "Temporary Suspension Requests Approved";
    public static final String CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS = "Permanent Suspension Requests Received";
    public static final String CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS = "Permanent Suspension Requests Approved";

    public static final String HP_PROFILE_ID = "hpProfileId";
    public static final String COLLEGE_ID = "collegeId";

    public static final String ID = "id";
    public static final String HP_PROFILE_STATUS_ID = "hp_profile_status_id";
    public static final String APPLICATION_STATUS_TYPE_ID = "application_status_type_id";
    public static final String VERIFIED_BY = "verified_by";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String USER_TYPE_ID = "user_type_id";
    public static final String USER_TYPE = "userType";

    public static final String USER_SUB_TYPE_ID = "user_sub_type_id";
    public static final String APPLICATION_TYPE_NAME = "applicationTypeName";
    public static final String GROUP_ID_COLUMN = "group_id";
    public static final String SMC_PROFILE_ID = "smcProfileId";
    public static final String GROUP_NAME = "groupName";
    public static final String WORK_FLOW_STATUS = "workFlowStatus";
    public static final String FETCH_DETAILS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow w JOIN registration_details rd ON w.hp_profile_id=rd.hp_profile_id " +
            "JOIN hp_profile hp ON w.hp_profile_id = hp.id " +
            "JOIN application_type a ON w.application_type_id = a.id " +
            "JOIN \"group\" g ON w.previous_group_id=g.id " +
            "JOIN work_flow_status ws ON w.work_flow_status_id= ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :" + GROUP_NAME + " " +
            "AND a.name = :" + APPLICATION_TYPE_NAME + " " +
            "AND ws.name = :" + WORK_FLOW_STATUS + " ";

    public static final String FETCH_DETAILS_WITH_PENDING_STATUS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow w JOIN registration_details rd ON w.hp_profile_id = rd.hp_profile_id " +
            "JOIN hp_profile hp ON w.hp_profile_id = hp.id " +
            "JOIN application_type a ON w.application_type_id = a.id " +
            "JOIN \"group\" g ON w.current_group_id = g.id " +
            "JOIN work_flow_status ws ON w.work_flow_status_id = ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :" + GROUP_NAME + " " +
            "AND a.name = :" + APPLICATION_TYPE_NAME + " " +
            "AND ws.name = :" + WORK_FLOW_STATUS + " ";

    public static final String FETCH_DETAILS_WITH_APPROVED_STATUS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow_audit wa JOIN registration_details rd ON w.hp_profile_id=rd.hp_profile_id " +
            "JOIN hp_profile hp ON wa.hp_profile_id = hp.id " +
            "JOIN application_type a ON wa.application_type_id = a.id " +
            "JOIN \"group\" g ON wa.previous_group_id=g.id " +
            "JOIN work_flow_status ws ON wa.work_flow_status_id= ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :" + GROUP_NAME + " " +
            "AND a.name = :" + APPLICATION_TYPE_NAME + " " +
            "AND ws.name = :" + WORK_FLOW_STATUS + " ";

    public static final String FETCH_CARD_DETAILS_QUERY = "select d.work_flow_status_id doctor_status, smc_status, nmc_status, nbe_status, d.hp_profile_id, d.request_id, rd.registration_no, rd.created_at, stmc.name, hp.full_name,  work_flow_status_id,(SELECT CASE WHEN ( wf.work_flow_status_id in(2, 4, 5, 6) ) THEN DATE_PART( 'day', (wf.updated_at - wf.created_at) ) WHEN ( wf.work_flow_status_id in(1, 3) ) THEN DATE_PART( 'day', (now() - wf.created_at) ) END FROM main.work_flow wf where wf.request_id = d.request_id ) as pendency, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date, college_status,d.application_type_id, count(*) OVER() AS total_count from main.dashboard d INNER JOIN main.registration_details as rd on rd.hp_profile_id = d.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id ";

    public static final String FETCH_TRACK_DETAILS_QUERY = "select d.work_flow_status_id doctor_status, smc_status, nmc_status, nbe_status, d.hp_profile_id, d.request_id, rd.registration_no, rd.created_at, stmc.name, hp.full_name, application_type_id, ( SELECT a.name FROM main.application_type a WHERE a.id = application_type_id ) as application_type_name, ( SELECT CASE WHEN ( wf.work_flow_status_id in(2, 4, 5, 6) ) THEN DATE_PART( 'day', (wf.updated_at - wf.created_at) ) WHEN ( wf.work_flow_status_id in(1, 3) ) THEN DATE_PART( 'day', (now() - wf.created_at) ) END FROM main.work_flow wf where wf.request_id = d.request_id ) as pendency, work_flow_status_id, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date, college_status, count(*) OVER() AS total_count from main.dashboard d INNER JOIN main.registration_details as rd on rd.hp_profile_id = d.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id ";
    public static final String STATE_MEDICAL_COUNCIL_ID = "state_medical_council_id";
    public static final String REGISTRATION_DETAILS_ID = "registration_details_id";
    public static final String SUCCESS = "Success";
    public static final String DCS_INTEGRATOR_NAME = "NMR";
    public static final String E_SIGN_SUCCESS_STATUS = "success";
    public static final String E_SIGN_FAILURE_STATUS = "failure";
    public static final String DSC_SERVICE = "dsc";
    public static final String DSC_SERVICE_ENDPOINT = "${dsc.endpoint.url}";
    public static final String GEN_ESP_REQUEST_URL = "/digiSign/genEspRequest";
    public static final String VERIFY_ESP_REQUEST_URL = "/digiSign/pdf/{tansactionId}";
    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final long LOCK_TIME_DURATION = 3; // hours
    public static final String ACCOUNT_LOCKED_MESSAGE = "Your account has been locked for " + LOCK_TIME_DURATION + " hours due to " + MAX_FAILED_ATTEMPTS + " failed attempts";
    public static final String ACCOUNT_UNLOCKED_MESSAGE = "Your account has been unlocked. Please try to login again";
    public static final String TEMPLATE_VAR1 = "var1";
    public static final String TEMPLATE_VAR2 = "var2";
    public static final String TEMPLATE_VAR3 = "var3";
    public static final String MESSAGE_SENDER = "National Medical Register.";
    public static final String NMR_ACCOUNT = "NMR Account";
    public static final String DEFAULT_COUNTRY_AADHAR = "India";
    public static final int DEFAULT_ADDRESS_TYPE_AADHAR = 4;
    public static final String INDIA = "India";
    public static final String INTERNATIONAL = "International";
    public static final String SALUTATION_DR = "Dr.";
    public static final String COUNCIL_NAME_IN_LOWER_CASE = "councilname";
    public static final String SEARCH_IN_LOWER_CASE = "search";
    public static final String APPLICATION_TYPE_ID_IN_LOWER_CASE = "applicationtypeid";
    public static final String REGISTRATION_NUMBER_IN_LOWER_CASE = "registrationnumber";
    public static final String GENDER_IN_LOWER_CASE = "gender";
    public static final String EMAIL_ID_IN_LOWER_CASE = "emailid";
    public static final String MOBILE_NUMBER_IN_LOWER_CASE = "mobilenumber";
    public static final String YEAR_OF_REGISTRATION_IN_LOWER_CASE = "yearofregistration";
    public static final String SMC_ID_IN_LOWER_CASE = "smcid";
    public static final String WORK_FLOW_STATUS_IN_LOWER_CASE = "workflowstatus";
    public static final String WORK_FLOW_STATUS_ID_IN_LOWER_CASE = "workflowstatusid";
    public static final String APPLICANT_FULL_NAME_IN_LOWER_CASE = "applicantfullname";

    public static final String FIRST_NAME_IN_LOWER_CASE = "firstname";
    public static final String LAST_NAME_IN_LOWER_CASE = "lastname";
    public static final String USER_TYPE_ID_IN_LOWER_CASE = "usertypeid";
    public static final String FETCH_REACTIVATION_REQUESTS = "SELECT hp.id, hp.registration_id, wf.request_id, hp.full_name, wf.created_at, wf.start_date, hp.gender, hp.email_id, hp.mobile_number , hp.hp_profile_status_id, wf.remarks, COUNT(*) OVER() AS total_count FROM main.work_flow AS wf INNER JOIN main.hp_profile AS hp ON wf.hp_profile_id = hp.id WHERE application_type_id = 5";
    public static final String NOT_NULL_ERROR_MSG = "The {0} is mandatory.";
    public static final String NOT_BLANK_ERROR_MSG = "The {0} should not be blank.";
    public static final String INVALID_PINCODE = "pincode should be of max 6 digit";
    public static final String FETCH_WORK_PROFILE_RECORDS_BY_USER_ID = """
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id, 
            state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at, 
            updated_at, request_id, facility_type_id, organization_type, registration_no, experience_in_years,delete_status FROM work_profile where delete_status=false AND  user_id =:userId""" ;
     public static final int MAX_DATA_SIZE = 500;
    public static final String DEFAULT_SORT_ORDER = "DESC";
    public static final String SMS_AND_EMAIL_RESET_MESSAGE_PROPERTIES_KEY = "sms-email-reset";
    public static final String ACCOUNT_CREATED = "account-created";
    public static final String NMR_ID_CREATED = "nmr-id-created";
    public static final String HPR_ACCOUNT_CREATED = "hpr-account-created";
    public static final String TYPE_NOT_NULL = "Type cannot be null";
    public static final double FUZZY_MATCH_LIMIT = 75;
    public static final String FUZZY_PARAMETER_NAME = "Name";
    public static final String FUZZY_PARAMETER_GENDER = "Gender";
    public static final String FUZZY_PARAMETER_DOB = "DOB";
    public static final Integer YES = 1;
    public static final Integer NO = 0;
    public static final String KAFKA_TOPIC = "esign_topic";
    public static final String KAFKA_GROUP_ID = "NMR_esign_group";
    public static final String GATEWAY_SERVICE = "gateway";
    public static final String GATEWAY_SERVICE_ENDPOINT = "${gateway.endpoint}";
    public static final String SESSION_URL = "/gateway/v0.5/sessions";
    public static final String VERIFIER_COLLEGE = " by College";
    public static final String VERIFIER_SMC = " by SMC";
    public static final String VERIFIER_NMC = " by NMC";
    public static final String VERIFIER_NBE = " by NBE";
    public static final String VERIFIER_SYSTEM = " by System";
    public static final String DOCTOR_QUALIFICATION = "MBBS";
    public static final String DOCTOR_QUALIFICATION_PATTERN = "[^A-Za-z]+";
    public static final String EMAIL_VERIFICATION_TEMPLATE = "email-verification-link";
    public static final String FETCH_COUNT_QUERY_FOR_SMC = "SELECT application_type_id as applicationTypeId, smc_status as profileStatus, count(*) as count FROM main.dashboard d join main.registration_details rd on rd.hp_profile_id = d.hp_profile_id where application_type_id in (1,2,3,4,7,8) and rd.state_medical_council_id =:" + SMC_PROFILE_ID + " group by application_type_id, smc_status";
    public static final String FETCH_COUNT_QUERY_FOR_COLLEGE = "SELECT application_type_id as applicationTypeId, college_status as profileStatus, count(*) as count FROM main.dashboard d join main.qualification_details qd on qd.hp_profile_id = d.hp_profile_id and qd.request_id = d.request_id where application_type_id in (1,8) and qd.college_id =:" + COLLEGE_ID + " group by application_type_id, college_status";
    public static final String FETCH_COUNT_QUERY_FOR_NMC = "SELECT application_type_id as applicationTypeId, nmc_status as profileStatus, count(*) as count FROM main.dashboard d where application_type_id in (1,2,3,4,7,8) group by application_type_id , nmc_status ";
    public static final String FETCH_COUNT_QUERY_FOR_NBE = "SELECT application_type_id as applicationTypeId, nbe_status as profileStatus, count(*) as count FROM main.dashboard d where application_type_id in (7) group by application_type_id , nbe_status ";
    public static final String NOT_YET_RECEIVED = "Not Yet Received";
    public static final String OPERATOR_MINUS = "-";
    public static final String OPERATOR_PLUS = "+";
    public static final String OPERATOR_MULTIPLICATION = "*";
    public static final List<String> OPERATORS = Arrays.asList(OPERATOR_PLUS, OPERATOR_MINUS, OPERATOR_MULTIPLICATION);
    public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String STATE_MEDICAL_COUNCIL_URL = "/state-medical-councils";
    public static final String FACILITY_ID = "facilityId";
    public static final String DELINK_FAILED ="Failed to Delink: Invalid Facility Id";
    public static final String  DELINK_WORK_PROFILE_BY_FACILITY_ID ="UPDATE work_profile SET delete_status =true  WHERE facility_id IN (:facilityId) AND user_id =:userId";

    public static final String FETCH_ACTIVE_WORK_PROFILE_RECORDS_BY_USER_ID = """            
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id,
            state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at,
            updated_at, request_id, facility_type_id, organization_type, registration_no, experience_in_years,delete_status FROM work_profile WHERE  delete_status=false AND user_id =:userId""";


    public static final String DEACTIVATE_USER = "update  {h-schema}user SET delete_status =true WHERE id =:userId";

    public static final String UNLOCK_USER ="update {h-schema}user SET failed_attempt =0, account_non_locked = true, lock_time = null WHERE id = :userId";

    public static final String FETCH_SMC_DETAILS = "select u.id, user_type_id ,sp.first_name, sp.last_name ,email,mobile_number,u.user_sub_type_id from main.user u join main.smc_profile sp on u.id =sp.user_id where u.delete_status =false ";
    public static final String FETCH_COLLEGE_DETAILS = "union select u.id ,user_type_id ,cp.name, '' ,email,mobile_number,u.user_sub_type_id from main.user u join main.college_profile cp on u.id =cp.user_id where u.delete_status =false ";
    public static final String FETCH_NMC_DETAILS = "union select u.id, user_type_id ,np.first_name, np.last_name,email ,mobile_number,u.user_sub_type_id from main.user u join main.nmc_profile np on u.id =np.user_id where u.delete_status =false ";
    public static final String FETCH_NBE_DETAILS = "union select u.id ,user_type_id ,nbep.first_name ,nbep.last_name,email ,mobile_number,u.user_sub_type_id from main.user u join main.nbe_profile nbep on u.id =nbep.user_id where u.delete_status =false ";
    public static final String REDIS_HOST = "${spring.redis.host}";
    public static final String REDIS_PASSWORD = "${spring.redis.password}";
    public static final String REDIS_PORT = "${spring.redis.port}";
    public static final String REDIS_DATABASE = "${spring.redis.database}";
    public static final String INVALID_FACILITY_DETAILS_MESSAGE = "Invalid facility details provided";
}