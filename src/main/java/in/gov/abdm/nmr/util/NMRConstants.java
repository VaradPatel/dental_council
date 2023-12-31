package in.gov.abdm.nmr.util;


import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class holds all the constants associated with NMR application
 */
@UtilityClass
public class NMRConstants {
    public static final String NOTIFICATION_REQUEST_MAPPING = "/notification";
    public static final String SEND_OTP = "/send-otp";
    public static final String VERIFY_OTP = "/verify-otp";
    public static final String RETRIEVE_USER = "/retrieve-user";
    public static final String FACILITY_SERVICE_SEARCH = "/v1.0/facility/search-facilities";
    public static final String FETCH_FACILITY_INFO = "/FacilityManagement/v1.0/facility/fetch-facility-info";
    public static final String NOTIFICATION_SERVICE_SEND_MESSAGE = "/internal/v3/notification/message";
    public static final String NOTIFICATION_DB_SERVICE_GET_TEMPLATE = "/internal/v3/notification/template/id/{id}";
    public static final String RESET_PASSWORD = "/user/reset-password";
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
    public static final String QUERY_CLOSED_STATUS = "closed";
    public static final String QUERY_OPEN_STATUS = "open";
    public static final String GLOBAL_NOTIFICATION_ENDPOINT = "${global.notification.endpoint}";
    public static final String RENEWABLE_REGISTRATION_CODE = "1";
    public static final String GLOBAL_FACILITY_ENDPOINT = "${global.facility.endpoint}";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String OTP_EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String INFO_EMAIL_SUBJECT = "NMR : Status Changed";
    public static final String INFO_EMAIL_VERIFICATION_SUCCESSFUL_SUBJECT = "NMR : Verification Successful";
    public static final String INFO_EMAIL_SET_PASSWORD_SUBJECT = "NMR : Set New Password";
    public static final String ACCOUNT_CREATED_SUBJECT = "Account Created";
    public static final String INCORRECT_E_SIGNED_SUBJECT = "Incorrectly E-signed";
    public static final String LICENCE_UPDATE_SUBJECT = "NMR Licence Update";
    public static final String VERIFY_EMAIL_SUBJECT = "NMR: Verify Email";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String SMS_OTP_MESSAGE_PROPERTIES_KEY = "sms-otp";
    public static final String EMAIL_OTP_MESSAGE_PROPERTIES_KEY = "email-otp";
    public static final String SMS_VERIFIED_MESSAGE_PROPERTIES_KEY = "sms-verified";
    public static final String EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY = "email-verified";
    public static final String STATUS_CHANGED_MESSAGE_PROPERTIES_KEY = "status-changed";
    public static final String INFO_CONTENT_TYPE = "info";
    public static final String CONTACT_NOT_NULL = "Please enter contact";
    public static final String OTP_NOT_NULL = "Please enter otp";
    public static final String USER_TYPE_NOT_NULL = "User type cannot be null";

    public static final String SUCCESS_RESPONSE = "Success";
    public static final String FAILURE_RESPONSE = "Fail";
    public static final String SENT_RESPONSE = "sent";
    public static final String USER_NOT_FOUND = "Please validate the provided input and try again";
    public static final String EMAIL_ALREADY_VERIFIED = "This email is already verified";
    public static final String EMAIL_USED_BY_OTHER_USER = "Email is already used by other user";
    public static final String MOBILE_USED_BY_OTHER_USER = "Mobile number is already used by other user";
    public static final String UPDATING_SAME_MOBILE_NUMBER = "Mobile number is already verified";
    public static final String FORBIDDEN = "Forbidden";
    public static final String LINK_EXPIRED = "Link expired";
    public static final String INVALID_LINK = "Invalid link";
    public static final String OLD_PASSWORD_NOT_MATCHING = "Old password is not correct";
    public static final String PROBLEM_OCCURRED = "Problem Occurred";
    public static final String USERNAME_NOT_NULL = "Username cannot be null or empty";
    public static final String PASSWORD_NOT_NULL = "Password cannot be null or empty";
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

    public static final String FETCH_CARD_DETAILS_QUERY = "select d.work_flow_status_id doctor_status, smc_status, nmc_status, nbe_status, d.hp_profile_id, d.request_id, rd.registration_no, d.created_at, stmc.name, hp.full_name, d.work_flow_status_id,(SELECT CASE WHEN ( wf.work_flow_status_id in(2, 4, 5, 6) ) THEN DATE_PART( 'day', (wf.updated_at - wf.created_at) ) WHEN ( wf.work_flow_status_id in(1, 3) ) THEN DATE_PART( 'day', (now() - wf.created_at) ) END FROM work_flow wf where wf.request_id = d.request_id ) as pendency, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date, college_status,d.application_type_id, count(*) OVER() AS total_count, workflow.start_date, workflow.end_date, workflow.remarks from dashboard d left join work_flow workflow on workflow.request_id = d.request_id INNER JOIN registration_details as rd on rd.hp_profile_id = d.hp_profile_id left join qualification_details as qd1 on qd1.request_id = d.request_id left join foreign_qualification_details fqd1 on fqd1.request_id =d.request_id INNER JOIN state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN hp_profile as hp on rd.hp_profile_id = hp.id and hp.e_sign_status IN (1,4,5) ";
    public static final String FETCH_CARD_DETAILS_QUERY_FOR_ADDITIONAL_QUALIFICATION = "select d.work_flow_status_id doctor_status, smc_status, nmc_status, nbe_status, d.hp_profile_id, d.request_id, rd.registration_no, d.created_at, stmc.name, hp.full_name, d.work_flow_status_id,(SELECT CASE WHEN ( wf.work_flow_status_id in(2, 4, 5, 6) ) THEN DATE_PART( 'day', (wf.updated_at - wf.created_at) ) WHEN ( wf.work_flow_status_id in(1, 3) ) THEN DATE_PART( 'day', (now() - wf.created_at) ) END FROM work_flow wf where wf.request_id = d.request_id ) as pendency, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date, college_status,d.application_type_id, count(*) OVER() AS total_count, workflow.start_date, workflow.end_date, workflow.remarks from dashboard d left join work_flow workflow on workflow.request_id = d.request_id INNER JOIN registration_details as rd on rd.hp_profile_id = d.hp_profile_id left join qualification_details as qd1 on qd1.request_id = d.request_id left join foreign_qualification_details fqd1 on fqd1.request_id =d.request_id INNER JOIN state_medical_council as stmc on qd1.state_medical_council_id = stmc.id INNER JOIN hp_profile as hp on rd.hp_profile_id = hp.id and hp.e_sign_status IN (1,4,5) ";

    public static final String FETCH_TRACK_DETAILS_QUERY = "select d.work_flow_status_id doctor_status, smc_status, nmc_status, nbe_status, d.hp_profile_id, d.request_id, rd.registration_no, d.created_at, stmc.name, hp.full_name, application_type_id, ( SELECT a.name FROM main.application_type a WHERE a.id = application_type_id ) as application_type_name, ( SELECT CASE WHEN ( wf.work_flow_status_id in(2, 4, 5, 6) ) THEN DATE_PART( 'day', (wf.updated_at - wf.created_at) ) WHEN ( wf.work_flow_status_id in(1, 3) ) THEN DATE_PART( 'day', (now() - wf.created_at) ) END FROM main.work_flow wf where wf.request_id = d.request_id ) as pendency, work_flow_status_id, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date, college_status, count(*) OVER() AS total_count from main.dashboard d INNER JOIN main.registration_details as rd on rd.hp_profile_id = d.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id ";
    public static final String STATE_MEDICAL_COUNCIL_ID = "state_medical_council_id";
    public static final String DCS_INTEGRATOR_NAME = "NMR";
    public static final String DSC_SERVICE = "dsc";
    public static final String DSC_SERVICE_ENDPOINT = "${dsc.endpoint.url}";
    public static final String GEN_ESP_REQUEST_URL = "/digiSign/genEspRequest";
    public static final String VERIFY_ESP_REQUEST_URL = "/digiSign/pdf/{tansactionId}";
    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final long LOCK_TIME_DURATION = 3;
    public static final String ACCOUNT_LOCKED_MESSAGE = "Your account has been locked for " + LOCK_TIME_DURATION + " hours due to " + MAX_FAILED_ATTEMPTS + " failed attempts";
    public static final String ACCOUNT_UNLOCKED_MESSAGE = "Your account has been unlocked. Please try to login again";
    public static final String TEMPLATE_VAR1 = "var1";
    public static final String TEMPLATE_VAR2 = "var2";
    public static final String TEMPLATE_VAR3 = "var3";
    public static final String MESSAGE_SENDER = "Regards,\nNational Medical Register,";
    public static final String NMR_ACCOUNT = "NMR Account";
    public static final String DEFAULT_COUNTRY_AADHAR = "India";
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
    public static final String NAME_IN_LOWER_CASE = "name";
    public static final String YEAR_OF_REGISTRATION_IN_LOWER_CASE = "yearofregistration";
    public static final String SMC_ID_IN_LOWER_CASE = "smcid";
    public static final String REQUEST_ID_IN_LOWER_CASE = "requestid";
    public static final String WORK_FLOW_STATUS_ID_IN_LOWER_CASE = "workflowstatusid";
    public static final String APPLICANT_FULL_NAME_IN_LOWER_CASE = "applicantfullname";
    public static final String FIRST_NAME_IN_LOWER_CASE = "firstname";
    public static final String LAST_NAME_IN_LOWER_CASE = "lastname";
    public static final String USER_TYPE_ID_IN_LOWER_CASE = "usertypeid";
    public static final String FETCH_REACTIVATION_REQUESTS = """
            select
            	hp.id,
            	hp.registration_id,
            	wf.request_id,
            	hp.full_name,
            	wf.created_at,
            	wf.start_date,
            	hp.gender,
            	hp.email_id,
            	hp.mobile_number ,
            	hp.hp_profile_status_id,
            	wf.remarks,
            	COUNT(*) over() as total_count,
            	ua.file_path,
            	ua.file_bytes,
            	ua.name
            from
            	main.work_flow as wf
            inner join main.hp_profile as hp on
            	wf.hp_profile_id = hp.id
            inner join main.user_attachments ua on
            	hp .user_id = ua.user_id and ua.request_id=wf.request_id
            where
            	application_type_id = 5
            """;
    public static final String NOT_NULL_ERROR_MSG = "The {0} is mandatory.";
    public static final String NOT_BLANK_ERROR_MSG = "The {0} should not be blank.";
    public static final String INVALID_PINCODE = "Invalid pincode should be of max 6 digit";
    public static final String FETCH_WORK_PROFILE_RECORDS_BY_USER_ID = """
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id, 
            state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at, 
            updated_at, request_id, facility_type_id, organization_type, registration_no, experience_in_years,delete_status, system_of_medicine, department, designation, reason, remark FROM work_profile where delete_status=false AND  user_id =:userId order by updated_at desc""" ;
     public static final int MAX_DATA_SIZE = 500;
    public static final String DEFAULT_SORT_ORDER = "DESC";
    public static final String SMS_AND_EMAIL_RESET_MESSAGE_PROPERTIES_KEY = "sms-email-reset";
    public static final String ACCOUNT_CREATED = "account-created";
    public static final String NMR_ID_CREATED = "nmr-id-created";
    public static final String HPR_ACCOUNT_CREATED = "hpr-account-created";
    public static final String INCORRECT_E_SIGNED = "incorrect-e-sign";
    public static final String LICENCE_STATUS = "licence-status";
    public static final String TYPE_NOT_NULL = "Please enter type";
    public static final String ACTION_REACTIVATED = "reactivated";
    public static final double FUZZY_MATCH_LIMIT = 75;
    public static final String FUZZY_PARAMETER_NAME = "Name";
    public static final String FUZZY_PARAMETER_GENDER = "Gender";
    public static final String FUZZY_PARAMETER_DOB = "DOB";
    public static final Integer YES = 1;
    public static final Integer NO = 0;
    public static final String KAFKA_TOPIC = "esign_topic";
    public static final String KAFKA_GROUP_ID = "NMR_esign_group";
    public static final String UNDERSCORE = "_";
    public static final String GATEWAY_SERVICE = "gateway";
    public static final String GATEWAY_SERVICE_ENDPOINT = "${gateway.endpoint}";
    public static final String SESSION_URL = "/gateway/v0.5/sessions";
    public static final String VERIFIER_COLLEGE = " by college";
    public static final String VERIFIER_SMC = " by SMC";
    public static final String VERIFIER_NMC = " by NMC";
    public static final String VERIFIER_NBE = " by NBE";
    public static final String VERIFIER_SYSTEM = " by system";
    public static final String DOCTOR_QUALIFICATION = "MBBS";
    public static final String DOCTOR_QUALIFICATION_PATTERN = "[^A-Za-z]+";
    public static final String EMAIL_VERIFICATION_TEMPLATE = "email-verification-link";
    public static final String FETCH_COUNT_QUERY_FOR_SMC = "SELECT application_type_id as applicationTypeId, smc_status as profileStatus, count(*) as count FROM main.dashboard d join main.registration_details rd on rd.hp_profile_id = d.hp_profile_id left join main.qualification_details qd on qd.request_id = d.request_id JOIN main.hp_profile hp on rd.hp_profile_id = hp.id and hp.e_sign_status IN (1,4,5) where (application_type_id in (1,3,4,7) and rd.state_medical_council_id =:" + SMC_PROFILE_ID + ") or application_type_id in (8) and qd.state_medical_council_id =:" + SMC_PROFILE_ID + " group by application_type_id, smc_status";
    public static final String FETCH_COUNT_QUERY_FOR_COLLEGE = "SELECT application_type_id as applicationTypeId, college_status as profileStatus, count(*) as count FROM main.dashboard d join main.qualification_details qd on qd.hp_profile_id = d.hp_profile_id and qd.request_id = d.request_id where application_type_id in (1,8) and qd.college_id =:" + COLLEGE_ID + " group by application_type_id, college_status";
    public static final String FETCH_COUNT_QUERY_FOR_NMC = "SELECT application_type_id as applicationTypeId, nmc_status as profileStatus, count(*) as count FROM main.dashboard d where application_type_id in (1,3,4,7,8) group by application_type_id , nmc_status ";
    public static final String FETCH_COUNT_QUERY_FOR_NBE = "SELECT application_type_id as applicationTypeId, nbe_status as profileStatus, count(*) as count FROM main.dashboard d where application_type_id in (7) group by application_type_id , nbe_status ";
    public static final String HYPHEN = "-";
    public static final String OPERATOR_MINUS = "-";
    public static final String OPERATOR_PLUS = "+";
    public static final String OPERATOR_MULTIPLICATION = "*";
    public static final List<String> OPERATORS = List.of(OPERATOR_PLUS, OPERATOR_MINUS, OPERATOR_MULTIPLICATION);
    public static final String RSA_PADDING = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
    public static final String STATE_MEDICAL_COUNCIL_URL = "/state-medical-councils";
    public static final String FACILITY_ID = "facilityId";
    public static final String DELINK_FAILED ="Failed to Delink: Invalid Facility Id";
    public static final String  DELINK_WORK_PROFILE_BY_FACILITY_ID ="UPDATE work_profile SET delete_status =true  WHERE facility_id IN (:facilityId) AND user_id =:userId";

    public static final String FETCH_ACTIVE_WORK_PROFILE_RECORDS_BY_USER_ID = """            
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id,
            state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at,
            updated_at, request_id, facility_type_id, organization_type, registration_no, experience_in_years,delete_status, system_of_medicine, department, designation, reason, remark FROM work_profile WHERE  delete_status=false AND is_user_currently_working=0 AND user_id =:userId""";


    public static final String DEACTIVATE_USER = "update  {h-schema}user SET delete_status =true WHERE id =:userId";

    public static final String UNLOCK_USER ="update {h-schema}user SET failed_attempt =0, account_non_locked = true, lock_time = null WHERE id = :userId";
    public static final String UPDATE_LAST_LOGIN_AND_RESET_FAILED_ATTEMPT_COUNT ="update {h-schema}user SET last_login=current_timestamp,failed_attempt=0 WHERE id = :userId";
    public static final String FETCH_SMC_DETAILS = "select u.id, user_type_id ,p.first_name, p.last_name ,email,mobile_number,u.user_sub_type_id,u.last_login ,a.name as CouncilName ,'' as collegeName,p.created_at from main.user u join main.smc_profile p on u.id =p.user_id JOIN state_medical_council a on p.state_medical_council_id = a.id where u.delete_status =false  ";
    public static final String FETCH_COLLEGE_DETAILS = " union select u.id ,user_type_id ,p.name, ''as last_name ,email,mobile_number,u.user_sub_type_id,u.last_login ,''as CouncilName ,a.name as collegeName ,p.created_at from user u join college_profile p on u.id =p.user_id JOIN college_master a on p.college_id = a.id where u.delete_status =false ";
    public static final String FETCH_NMC_DETAILS = " union select u.id, user_type_id ,p.first_name, p.last_name,email ,mobile_number,u.user_sub_type_id,u.last_login ,'','',p.created_at from user u join nmc_profile p on u.id =p.user_id where u.delete_status =false ";
    public static final String FETCH_NBE_DETAILS = "union select u.id ,user_type_id ,p.first_name ,p.last_name,email ,mobile_number,u.user_sub_type_id,u.last_login,'','',p.created_at from user u join nbe_profile p on u.id =p.user_id where u.delete_status =false";
    public static final String REDIS_HOST = "${spring.redis.host}";
    public static final String REDIS_PASSWORD = "${spring.redis.password}";
    public static final String REDIS_PORT = "${spring.redis.port}";
    public static final String REDIS_DATABASE = "${spring.redis.database}";
    public static final String INVALID_FACILITY_PAYLOAD_MESSAGE = "Either id OR state, district and ownership is required";



    public static final String BEARER= "Bearer ";
    public static final String HPR_REGISTER_SUCCESS="Registration of health professional in HPR is successful ";
    public static final String HPR_REGISTER_FAILED="Failed- Registration of health professional in HPR ";
    public static final String HPR_REGISTER_MISSING_VALUES ="HPR registration failed due to some missing values in the request payload of HPR ";

    public static final String HPR_ID_SERVICE = "HPRID";
    public static final String HPR_SERVICE = "HPR";
    public static final String HPR_ID_SERVICE_ENDPOINT = "${hprId.fetchToken.endpoint}";
    public static final String HPR_SERVICE_ENDPOINT = "${hpr.registerProfessional.endpoint}";

    public static final String HPR_ID="hpr_id";
    public static final String DOMAIN_NAME="@hpr.abdm";
    public static final String  HPR_SERVICE_REGISTER_HEALTH_PROFESSIONAL="/apis/v1/doctors/register-professional";
    public static final String  HPR_ID_SERVICE_TOKEN_BY_HPRID="/api/v2/search/internal/hfr/getTokensByHprId";
    public static final int QUALIFICATION_STATUS_PENDING = 0;
    public static final int QUALIFICATION_STATUS_APPROVED = 1;
    public static final int QUALIFICATION_STATUS_REJECTED = 2;

    public static final String MASTER_CACHE_NAME ="nmr-master-data";
    public static final String TEMPLATE_CACHE_NAME = "nmr-template-cache";
    public static final long MASTER_CACHE_CRON_TIME = 6L * 60 * 60 * 1000;
    public static final long TEMPLATE_CACHE_CRON_TIME = 24;
    public static final String REGEX_FOR_BIRTH_DATE = "^(((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))[-/]?[0-9]{4}|02[-/]?29[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$";
    public static final String REGEX_FOR_NAME = "\\b([a-zA-ZÀ-ÿ]|[-,a-zA-Z0-9. ']+[ ]*)+";
    public static final String REGEX_FOR_REGISTRATION_NUMBER = "^[a-zA-Z0-9 !-@#:_)(]{1,100}$";
    public static final String REGEX_FOR_ADDRESS = "^[#.0-9a-zA-Z\s,-/:()]+$";
    public static final String REGEX_FOR_SUB_DISTRICT = "^[A-Z a-z]+[A-Z a-z //' ']*$";
    public static final String REGEX_FOR_VILLAGE = "^[.A-Z a-z]+[A-Z a-z //' ']*$";
    public static final String REGEX_FOR_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final Integer MAX_QUALIFICATION_SIZE = 8;
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";

    public static final String PENDING = "Pending";
    public static final String FORWARDED = "Forwarded";
    public static final String QUERY_RAISED = "Query Raised";

    public static final String APPROVED = "Approved";
    public static final String REJECTED = "Rejected";
    public static final String BLACKLISTED = "Blacklisted";
    public static final String SUSPENDED = "Suspended";
    public static final String COLLEGE_NBE_VERIFIED = "College/NBE Verified";
    public static final String SUBMITTED = "Submitted";
    public static final String VERIFIED =  "Verified";
    public static final String SYSTEM_REJECTION_REMARK =  "Rejected by system due to profile suspension";


    public static final String APPLICATION_REQUEST_DETAILS = """
            Select wfa.request_id, wfa.application_type_id,wfa.created_at ,wfa.work_flow_status_id ,wfa.current_group_id ,
            wfa.action_id ,wfa.previous_group_id ,wfa.remarks ,c.course_name indian_qualification ,
            fqd.course international_qualification
            from work_flow_audit wfa left join qualification_details qd on qd.request_id = wfa.request_id 
            left join course c on c.id =qd.course_id 
            left join foreign_qualification_details fqd on fqd.request_id =wfa.request_id 
            where wfa.request_id = :requestId order by wfa.created_at asc
            """;

    public static final String DATE_FORMAT = "dd-MM-yyyy";

    public static final String SELECT_COUNTRY = "Please select a country.";
    public static final String SELECT_STATE = "Please select a state.";
    public static final String SELECT_COLLEGE = "Please select a college.";
    public static final String SELECT_UNIVERSITY = "Please select a university.";
    public static final String SELECT_COURSE = "Please select a course.";
    public static final String SELECT_QUALIFICATION_YEAR = "Please select a qualification year.";
    public static final String SELECT_QUALIFICATION_MONTH = "Please select a qualification month.";
    public static final String SELECT_QUALIFICATION_FROM = "Please enter qualification degree completed.";
    public static final String SELECT_QUALIFICATION_DETAIL_REQUEST = "qualificationDetailRequestTos cannot be empty";
    public static final String SELECT_REGISTRATION_DATE = "Please select a registration date.";
    public static final String SELECT_REGISTRATION_NUMBER = "Please enter a registration number";
    public static final String INVALID_HOUSE = "Please enter a valid house name.";
    public static final String INVALID_STREET = "Please enter a valid street.";
    public static final String INVALID_LOCALITY = "Please enter a valid locality.";
    public static final String INVALID_LANDMARK = "Please enter a valid landmark.";
    public static final String INVALID_COUNTRY = "Please enter a valid country.";
    public static final String INVALID_FIRST_NAME = "Please enter a valid first name.";
    public static final String INVALID_MIDDLE_NAME = "Please enter a valid middle name.";
    public static final String INVALID_LAST_NAME = "Please enter a valid last name.";
    public static final String INVALID_FATHER_NAME = "Please enter a valid father's name.";
    public static final String INVALID_MOTHER_NAME = "Please enter a valid mother's name.";
    public static final String INVALID_SPOUSE_NAME = "Please enter a valid spouse name.";
    public static final String INVALID_FULL_NAME = "Please enter a valid full name.";
    public static final String DOCTOR_CURRENTLY_WORKING = "0";
    public static final String DOCTOR_CURRENTLY_NOT_WORKING = "1";
    public static final String ACTION_ID_ERROR_MSG = "Please enter a valid action id. supported action ids are 1, 2, 3, 4, 5, 6, 7.";
    public static final String ACTOR_ID_ERROR_MSG = "Please enter a valid actor id. supported actor ids are 1, 2, 3, 4, 7, 8";
    public static final String ADDRESS_ERROR_MSG = "Please enter a valid address. it must be alphanumeric with allowed special characters (.,-:/()).";
    public static final String ADDRESS_TYPE_ERROR_MSG = "Please enter a valid address type. Supported address types are 'business', 'current', 'permanent', 'communication', 'KYC'.";
    public static final String APPLICATION_TYPE_ID_ERROR_MSG = "Please enter a valid application type id. supported action ids are 1, 2, 3, 4, 5, 6, 7, 8.";
    public static final String EMAIL_ERROR_MSG = "Please enter a valid email address.";
    public static final String NAME_ERROR_MSG = "Please enter a valid name. it should be plain text.";
    public static final String OPTIONAL_ADDRESS_ERROR_MSG = "Please enter a valid address. it must be alphanumeric with allowed special characters (.,-:/()).";
    public static final String OPTIONAL_NAME_ERROR_MSG = "Please enter a valid input. it should be plain text.";
    public static final String PIN_CODE_ERROR_MSG = "Please enter a valid pin code. it must contain only numbers and have a maximum length of 6.";
    public static final String REGISTRATION_NUMBER_ERROR_MSG = "Please enter a valid registration number.";
    public static final String SALUTATION_ERROR_MSG = "Please enter a valid Salutation. Please use 'Dr.' ";
    public static final String SUB_DISTRICT_ERROR_MSG = "Please enter a valid sub district. it must contain only alphabets.";
    public static final String VILLAGE_ERROR_MSG = "Please enter a valid village. it must contain only alphabets";
    public static final String INVALID_USERNAME_ERROR_MSG = "Invalid username";
    public static final String NOT_ALLOWED_ERROR_MSG = "Not allowed";
    public static final String INVALID_CAPTCHA_ERROR_MSG = "Invalid captcha";
    public static final String BEARER_TOKEN_ERROR_MSG = "Invalid bearer token format";
    public static final String NO_BEARER_TOKEN_ERROR_MSG = "No bearer token was passed";
    public static final String INVALID_BEARER_TOKEN_ERROR_MSG = "Unable to authenticate token";
    public static final String UNABLE_TO_AUTHENTICATE_BEARER_TOKEN_ERROR_MSG = "Unable to authenticate JWT token";
    public static final String INVALID_TOKEN_ERROR_MSG = "Invalid token";
    public static final String EXPIRED_TOKEN_ERROR_MSG = "Token expired";
    public static final String INCORRECT_USERNAME_OR_PASSWORD_ERROR_MSG = "Incorrect username or password";
    public static final String EXCEPTION_IN_PARSING_BEARER_TOKEN = "Exception occurred while parsing bearer token";
    public static final String EXCEPTION_IN_AUTHENTICATING_JWT_TOKEN = "Exception occurred while authenticating JWT token";
    public static final String EXCEPTION_IN_PARSING_USERNAME_PASSWORD = "Exception occurred while parsing username-password login request";
    public static final String AUTHORIZATION = "Authorization";
    public static final String QUERY_RAISED_ERROR = "Please add query against atleast one field";
    private static final String MBBS_REGEX =  "(?i)M[-.]?B[-.]?B[-.]?S";
    public static final Pattern MBBS_PATTERN_MATCHER =  Pattern.compile(MBBS_REGEX);
    public static final List<String> MBBS_QUALIFICAITON_NAMES = List.of("MBBS - Bachelor of Medicine and Bachelor of Surgery", "BACHELOR OF MEDICINE AND BACHELOR OF SURGERY" );
    public static final String EMAIL_SUCCESS_PAGE = "email-verification-success";
    public static final String EMAIL_FAILURE_PAGE = "email-verification-failure";
    public static final String REGEX_FOR_ALPHANUMERIC = "^[a-zA-Z0-9\s.]*$";
    public static final String REGEX_FOR_EXTRA_SPACES_AND_NEW_LINES = "(\\r\\n|\\r|\\n|\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$))";
    public static final String INVALID_FILE = "Invalid file";
    public static final String INVALID_FILE_EXTENSION = " is not an allowed file type.";
    public static final String REGEX_FOR_FILE = "^[A-Za-z0-9 ()_-]+.[A-Za-z]+$";
    public static final String REGEX_FOR_URL = "^((https?)://|(www\\.)?)?[a-z0-9-]+(\\.[a-z0-9-]+)+(\\/[a-z0-9-]+)*$";

    public static final String IS_SMC_HAVE_ACCESS_FOR_VIEWING_HP_PROFILE = """
            SELECT
                   CASE WHEN EXISTS
                   (
                       (select hp_profile_id
               from
               	registration_details rd
                    join smc_profile sp on
               	rd.state_medical_council_id = sp.state_medical_council_id
               	where sp.user_id = :userId and rd.hp_profile_id= :hpProfileId)
               	UNION
               	(select hp_profile_id from  qualification_details qd 
               	join smc_profile sp on
               	qd.state_medical_council_id = sp.state_medical_council_id
               	where sp.user_id=:userId and qd.hp_profile_id= :hpProfileId)
               	UNION
               	(select hp_profile_id from foreign_qualification_details fqd
               	join smc_profile sp on
               	fqd.state_medical_council_id = sp.state_medical_council_id
               	where
                   sp.user_id = :userId and fqd.hp_profile_id = :hpProfileId)
                   )
                   THEN 'TRUE'
                   ELSE 'FALSE'
               end
            """;
}