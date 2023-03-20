package in.gov.abdm.nmr.util;


import lombok.experimental.UtilityClass;

import static in.gov.abdm.nmr.enums.ApplicationType.COLLEGE_REGISTRATION;
import static in.gov.abdm.nmr.enums.ApplicationType.HP_ACTIVATE_LICENSE;

/**
 * This class holds all the constants associated with NMR application
 */
@UtilityClass
public class NMRConstants {

    public static final String DASHBOARD_REQUEST_URL = "/dashboard";

    public static final String FETCH_COUNT_ON_CARD_URL = "/cardCount";

    public static final String FETCH_SPECIFIC_DETAILS_URL = "/fetchSpecificDetails";
    public static final String FETCH_TRACK_APP_URL = "/fetchTrackApplicationDetails";

    public static final String FETCH_DETAILS_BY_REG_NO_URL = "/fetchDetailsByRegNo";

    public static final String NOTIFICATION_REQUEST_MAPPING = "/notification";
    public static final String AADHAAR_REQUEST_MAPPING = "/aadhaar";
    public static final String SEND_OTP = "/send-otp";
    public static final String VERIFY_OTP = "/verify-otp";
    public static final String GENERATE_OTP = "/generateOtp";
    public static final String GENERATE_AADHAR_OTP = "/sendAadhaarOtp";
    public static final String VALIDATE_OTP = "/validateOtp";
    public static final String VALIDATE_AADHAR_OTP = "/verifyAadhaarOtp";
    public static final String FACILITY_SERVICE_SEARCH = "/v1.5/facility/search";
    public static final String AADHAR_SERVICE_SEND_OTP = "/api/v3/aadhaar/sendOtp";
    public static final String AADHAR_SERVICE_VERIFY_OTP = "/api/v3/aadhaar/verifyOtp";
    public static final String NOTIFICATION_SERVICE_SEND_MESSAGE = "/internal/v3/notification/message";
    public static final String NOTIFICATION_DB_SERVICE_GET_TEMPLATE = "/internal/v3/notification/template/id/{id}";
    public static final String RESET_PASSWORD = "/user/reset-password";
    public static final String PATH_HP_PROFILE = "/health-professional/user";

    public static final String RAISE_QUERY = "/health-professional/queries";
    public static final String SAVE_KYC_DETAILS = "/health-professional/{registrationNumber}/kyc";
    public static final String GET_QUERIES = "health-professional/{healthProfessionalId}/queries";
    public static final String PASSWORD_LINK = "/user/password-link";
    public static final String SET_PASSWORD = "/user/set-password";
    public static final String E_SIGN = "/e-signature";

    public static final String PATH_FACILITY_ROOT = "/facilities";
    public static final String PATH_FACILITY_SEARCH = "/search";

    public static final String PATH_COLLEGE_APPLICATIONS = "/college/applications";
    public static final String APPLICATION_REQUEST_URL = "/health-professional/applications";
    public static final String PATH_TRACK_APPLICATIONS_STATUS = APPLICATION_REQUEST_URL + "/applications";

    public static final String SUSPENSION_REQUEST_URL = APPLICATION_REQUEST_URL + "/suspend";
    public static final String REACTIVATE_REQUEST_URL = APPLICATION_REQUEST_URL + "/re-activate";
    public static final String HEALTH_PROFESSIONAL_ACTION = APPLICATION_REQUEST_URL + "/status";
    public static final String COLLEGES_ACTION = "/college/applications/status";
    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String FACILITY_SERVICE = "facility";
    public static final String NOTIFICATION_DB_SERVICE = "notification-db";

    public static final String LGD_DB_SERVICE = "lgd-db";
    public static final String AADHAAR_SERVICE = "aadhaar";
    public static final String CLOSED_STATUS = "closed";
    public static final String OPEN_STATUS = "open";
    public static final String GLOBAL_NOTIFICATION_ENDPOINT = "${global.notification.endpoint}";

    public static final String GLOBAL_LGD_ENDPOINT = "${global.lgd.endpoint}";

    public static final String LGD_API_VERSION = "/internal/v3/abdm";

    public static final String LGD = "/lgd";

    public static final String LGD_SEARCH_URL = LGD + "/search";

    public static final String LGD_FEIGN_SEARCH_URL = LGD_API_VERSION + LGD_SEARCH_URL;

    public static final String PIN_CODE = "pinCode";
    public static final String STATE_CODE = "stateCode";
    public static final String DISTRICT_CODE = "districtCode";

    public static final String VIEW = "view";
    public static final String GLOBAL_AADHAAR_ENDPOINT = "${global.aadhaar.endpoint}";
    public static final String GLOBAL_FACILITY_ENDPOINT = "${global.facility.endpoint}";
    public static final String OTP_GENERATION_EXCEEDED = "OTP Generation Attempts Exceeded";
    public static final String OTP_ATTEMPTS_EXCEEDED = "OTP Attempts Exceeded";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String NO_SUCH_TYPE = "No such type";
    public static final String OTP_EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String INFO_EMAIL_SUBJECT = "NMR : Status Changed";
    public static final String INFO_EMAIL_VERIFICATION_SUCCESSFUL_SUBJECT = "NMR : Verification Successful";
    public static final String INFO_EMAIL_SET_PASSWORD_SUBJECT = "NMR : Set New Password";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String SMS_OTP_MESSAGE_PROPERTIES_KEY = "sms-otp";
    public static final String EMAIL_OTP_MESSAGE_PROPERTIES_KEY = "email-otp";
    public static final String SMS_VERIFIED_MESSAGE_PROPERTIES_KEY = "sms-verified";
    public static final String EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY = "email-verified";
    public static final String STATUS_CHANGED_MESSAGE_PROPERTIES_KEY = "status-changed";
    public static final String SMS_RESET_PASSWORD_MESSAGE_PROPERTIES_KEY = "sms-reset";
    public static final String EMAIL_RESET_PASSWORD_MESSAGE_PROPERTIES_KEY = "email-reset";
    public static final String INFO_CONTENT_TYPE = "info";
    public static final String COLLEGE_PREFIX_TO_GET_WORKFLOW_STATUS = "college-";
    public static final String CONTACT_NOT_NULL = "Contact cannot be null or empty";
    public static final String AADHAR_NOT_NULL = "AADHAR Number cannot be null or empty";
    public static final String HP_PROFILE_NOT_NULL = "HP Profile ID cannot be null or empty";
    public static final String EMAIL_NOT_NULL = "Email cannot be null or empty";
    public static final String MOBILE_NOT_NULL = "Mobile cannot be null or empty";
    public static final String TRANSACTION_ID_NOT_NULL = "Transaction Id cannot be null or empty";
    public static final String OTP_NOT_NULL = "OTP cannot be null or empty";
    public static final String SUCCESS_RESPONSE = "Success";
    public static final String FAILURE_RESPONSE = "Fail";
    public static final String SENT_RESPONSE = "sent";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String COLLEGE_CONSTANT = "College";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INVALID_COLLEGE_ID = "Invalid college id";
    public static final String INVALID_PROFILE_ID = "Invalid profile id";
    public static final String LINK_EXPIRED = "Link expired";
    public static final String WORKFLOW_STATUS_NOT_FOUND = "Workflow status not found";
    public static final String TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES = "Template id not found in properties";
    public static final String TEMPLATE_NOT_FOUND = "Template not found";
    public static final String OLD_PASSWORD_NOT_MATCHING = "Old password not matching";
    public static final String PROBLEM_OCCURRED = "Problem Occurred";
    public static final String USER_ID_NOT_NULL = "User Id cannot be null or empty";
    public static final String USERNAME_NOT_NULL = "Username cannot be null or empty";
    public static final String REG_NUM_NOT_NULL = "Registration Number cannot be null or empty";
    public static final String PASSWORD_NOT_NULL = "Password cannot be null or empty";
    public static final String TOKEN_NOT_NULL = "Token cannot be null or empty";
    public static final int RESET_PASSWORD_LINK_EXPIRY_HOURS = 24;
    public static final String OTP_INVALID = "Invalid OTP";
    public static final String OTP_NOT_FOUND = "OTP Not Found";
    public static final String OTP_EXPIRED = "OTP Expired Or Not Found";
    public static final String TEMPLATE_ID = "templateId";
    public static final String SUBJECT = "subject";

    public static final String USER_ID = "userId";
    public static final String CONTENT = "content";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String MOBILE = "mobile";
    public static final String EMAIL_ID = "emailId";

    public static final String SMS = "sms";
    public static final String EMAIL = "email";
    public static final int OTP_GENERATION_MAX_ATTEMPTS = 5;
    public static final int OTP_MAX_ATTEMPTS = 3;

    public static final String TOTAL_HP_REGISTRATION_REQUESTS = "Total Registration Requests";
    public static final String TOTAL_HP_MODIFICATION_REQUESTS = "Total Modification Requests";

    public static final String TOTAL_TEMPORARY_SUSPENSION_REQUESTS = "Total Temporary Suspension Requests";

    public static final String TOTAL_PERMANENT_SUSPENSION_REQUESTS = "Total Permanent Suspension Requests";

    public static final String TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS = "Total Suspension Requests";

    public static final String CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS = "Temporary Suspension Requests Received";

    public static final String CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS = "Temporary Suspension Requests Approved";

    public static final String CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS = "Permanent Suspension Requests Received";

    public static final String CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS = "Permanent Suspension Requests Approved";

    public static final String TOTAL_ACTIVATE_LICENSE_REQUESTS = "Total Activate License Requests";

    public static final String TOTAL_COLLEGE_REGISTRATION_REQUESTS = "Total College Registration Requests";

    public static final String TOTAL_FOREIGN_HP_REGISTRATION_REQUESTS = "Total Foreign Registration Requests";

    public static final String HP_PROFILE_ID = "hpProfileId";

    public static final String COLLEGE_DEAN_ID = "collegeDeanId";

    public static final String COLLEGE_REGISTRAR_ID = "collegeRegistrarId";

    public static final String COLLEGE_ID = "collegeId";

    public static final String ID = "id";

    public static final String HP_PROFILE_STATUS_ID = "hp_profile_status_id";

    public static final String APPLICATION_STATUS_TYPE_ID = "application_status_type_id";

    public static final String VERIFIED_BY = "verified_by";

    public static final String SCHEDULE_ID = "schedule_id";

    public static final String USER_TYPE_ID = "user_type_id";

    public static final String USER_TYPE = "userType";

    public static final String USER_SUB_TYPE = "userSubType";

    public static final String USER_SUB_TYPE_ID = "user_sub_type_id";

    public static final String APPLICATION_STATUS_TYPE = "applicationStatusType";

    public static final String APPLICATION_TYPE_ID = "applicationTypeId";
    public static final String APPLICATION_TYPE_NAME = "applicationTypeName";

    public static final String GROUP_ID_COLUMN = "group_id";

    public static final String GROUP_ID = "groupId";

    public static final String SMC_PROFILE_ID = "smcProfileId";

    public static final String NBE_PROFILE_ID = "nbeProfileId";

    public static final String GROUP_NAME = "groupName";

    public static final String WORK_FLOW_STATUS = "workFlowStatus";
    public static final String FETCH_STATUS_WISE_COUNT_QUERY = """
            SELECT wfs.name, coalesce(result.count,0) as count FROM 
            (SELECT ws.name as name, COUNT(w) as count
            FROM work_flow w JOIN work_flow_status ws ON w.work_flow_status_id = ws.id
            WHERE w.application_type_id = :""" + APPLICATION_TYPE_ID + " AND w.current_group_id = :" + GROUP_ID + " OR ( w.application_type_id = :" + APPLICATION_TYPE_ID + " AND w.previous_group_id = :" + GROUP_ID + """ 
             \s AND w.action_id IN ( 3,5 ) )
            GROUP BY ws.name
            UNION
            SELECT 'Approved' as name, COUNT(wa) as count
            FROM work_flow_audit wa
            WHERE wa.application_type_id = :""" + APPLICATION_TYPE_ID + " AND wa.previous_group_id = :" + GROUP_ID + """
             \s AND wa.action_id = 4
            GROUP BY name ) as result right join main.work_flow_status wfs on wfs.name=result.name""";

    public static final String FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_COLLEGE = """
            SELECT wfs.name, coalesce (result.count,0) as count FROM 
            (SELECT ws.name as name, COUNT(w) as count 
            FROM work_flow w JOIN work_flow_status ws ON w.work_flow_status_id = ws.id 
            INNER JOIN qualification_details as qd on qd.hp_profile_id = w.hp_profile_id AND qd.request_id = w.request_id 
            WHERE w.application_type_id = :""" + APPLICATION_TYPE_ID + " AND qd.college_id = :" + COLLEGE_ID + " AND w.current_group_id = :" + GROUP_ID + " OR ( w.previous_group_id = :" + GROUP_ID + """ 
            \s AND w.action_id IN ( 3,5 ) )
            GROUP BY ws.name 
            UNION 
            SELECT 'Approved' as name, COUNT(wa) as count FROM work_flow_audit wa 
            INNER JOIN qualification_details as qd on qd.hp_profile_id = wa.hp_profile_id AND qd.request_id = wa.request_id  
            WHERE wa.application_type_id = :""" + APPLICATION_TYPE_ID + " AND wa.previous_group_id = :" + GROUP_ID + """  
            \s AND wa.action_id = 4 AND qd.college_id = :""" + COLLEGE_ID + " GROUP BY name ) as result right join main.work_flow_status wfs on wfs.name=result.name";

    public static final String FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_NBE = """
            SELECT wfs.name, coalesce(result.count,0) as count FROM 
            (SELECT ws.name as name, COUNT(w) as count 
            FROM work_flow w JOIN work_flow_status ws ON w.work_flow_status_id = ws.id 
            JOIN nbe_profile nbe ON nbe.user_id=w.user_id 
            WHERE nbe.id=:""" + NBE_PROFILE_ID + " AND w.application_type_id = :" + APPLICATION_TYPE_ID + " AND  w.current_group_id = :" + GROUP_ID + " OR ( w.previous_group_id = :" + GROUP_ID + """ 
             \s AND w.action_id IN ( 3,5 ) ) 
            GROUP BY ws.name 
            UNION 
            SELECT 'Approved' as name, COUNT(wa) as count FROM work_flow_audit wa 
            JOIN nbe_profile nbe ON wa.user_id=nbe.user_id 
            WHERE nbe.id=:""" + NBE_PROFILE_ID + " AND wa.application_type_id = :" + APPLICATION_TYPE_ID + " AND wa.previous_group_id = :" + GROUP_ID + """  
             \s AND wa.action_id = 4 
            GROUP BY name ) as result right join main.work_flow_status wfs on wfs.name=result.name""";

    public static final String FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_SMC = """
            SELECT wfs.name, coalesce(result.count,0) as count FROM 
            (SELECT ws.name as name, COUNT(w) as count 
            FROM work_flow w JOIN work_flow_status ws ON w.work_flow_status_id = ws.id 
            JOIN registration_details rd ON w.hp_profile_id=rd.hp_profile_id 
            WHERE rd.state_medical_council_id=:""" + SMC_PROFILE_ID + " AND w.application_type_id = :" + APPLICATION_TYPE_ID + " AND w.current_group_id = :" + GROUP_ID + " OR ( w.application_type_id = :" + APPLICATION_TYPE_ID + " AND w.previous_group_id = :" + GROUP_ID + """ 
             \s AND w.action_id IN ( 3,5 ) ) 
            OR (w.application_type_id =:""" + APPLICATION_TYPE_ID + """
             AND w.previous_group_id = 4 AND w.action_id = 4 ) GROUP BY ws.name 
            UNION 
            SELECT 'Approved' as name, COUNT(wa) as count FROM work_flow_audit wa 
            JOIN registration_details rd ON wa.hp_profile_id=rd.hp_profile_id 
            WHERE rd.state_medical_council_id=:""" + SMC_PROFILE_ID + " AND wa.application_type_id = :" + APPLICATION_TYPE_ID + " AND wa.previous_group_id = :" + GROUP_ID + """
             \s AND wa.action_id = 4 
            GROUP BY name ) as result right join main.work_flow_status wfs on wfs.name=result.name""";


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

    public static final String FETCH_COLLEGE_REGISTRATION_RECORDS = """
             SELECT c.id AS Id, c.college_code AS College_Id, c.name AS College_Name, smc.name AS Council_Name, wfs.name AS Status, wf.created_at AS Date_Of_Submission, 
            CASE WHEN (wf.updated_at> wf.created_at) THEN DATE_PART('day', (wf.updated_at- wf.created_at)) 
               WHEN (wf.updated_at= wf.created_at) THEN DATE_PART('day', (now()- wf.created_at))END as pendency , wf.request_id as request_id
              FROM work_flow wf  INNER JOIN colleges c  ON c.request_id= wf.request_id 
               INNER JOIN  state_medical_council smc ON smc.id =c.state_medical_council 
               JOIN work_flow_status wfs ON wfs.id= wf.work_flow_status_id  
               WHERE  wf.application_type_id= """ + COLLEGE_REGISTRATION.getId() + "  AND  c.state_medical_council IS NOT NULL ";

    public static final String FETCH_COUNT_OF_COLLEGE_REGISTRATION_RECORDS = """
            SELECT COUNT(*)  
              FROM work_flow wf  INNER JOIN colleges c  ON c.request_id= wf.request_id  
              INNER JOIN  state_medical_council smc ON smc.id =c.state_medical_council 
             JOIN work_flow_status wfs ON wfs.id= wf.work_flow_status_id  
             WHERE  wf.application_type_id= """ + COLLEGE_REGISTRATION.getId() + "  AND  c.state_medical_council IS NOT NULL ";

    public static final String FETCH_DETAILS_BY_REG_NO_QUERY = "SELECT hvs.registrationDetails.registrationNo as registrationNo, hvs.hpProfile.fullName as nameOfApplicant, hvs.registrationDetails.stateMedicalCouncil.name as nameOfStateCouncil, hvs.registrationDetails.registrationDate as dateOfSubmission, hvs.verifiedBy.userType.name as verifiedByUserType, hvs.verifiedBy.userSubType.name as verifiedByUserSubType, hvs.hpProfile.hpProfileStatus.name as hpProfileStatus " +
            "FROM HpVerificationStatus hvs " +
            "WHERE hvs.registrationDetails.registrationNo=:registrationNumber" +
            "AND hvs.registrationDetails.stateMedicalCouncil.name =:smcName " +
            "AND hvs.verifiedBy.userType.name =:userType " +
            "AND hvs.verifiedBy.userSubType.name =:userSubType ";

    public static final String FETCH_DETAILS_FOR_NMC_BY_REG_NO_QUERY = "SELECT hvs.registrationDetails.registrationNo as registrationNo, hvs.hpProfile.fullName as nameOfApplicant, hvs.registrationDetails.stateMedicalCouncil.name as nameOfStateCouncil, hvs.registrationDetails.registrationDate as dateOfSubmission, hvs.verifiedBy.userType.name as verifiedByUserType, hvs.verifiedBy.userSubType.name as verifiedByUserSubType, hvs.hpProfile.hpProfileStatus.name as hpProfileStatus " +
            "FROM HpVerificationStatus hvs " +
            "WHERE hvs.registrationDetails.registrationNo=:registrationNumber" +
            "AND hvs.verifiedBy.userType.name =:userType " +
            "AND hvs.verifiedBy.userSubType.name =:userSubType ";

    public static final String FETCH_CARD_DETAILS_QUERY = "select doctor_status, smc_status, college_dean_status, college_registrar_status, nmc_status, nbe_status, calculate.hp_profile_id, calculate.request_id, rd.registration_no, rd.created_at, stmc.name, hp.full_name, work_flow_status_id, DATE_PART('day', (now() - TO_TIMESTAMP(rd.created_at, 'YYYY-MM-DD HH24:MI:SS'))) as pendency, hp.gender, hp.email_id, hp.mobile_number,hp.nmr_id, rd.registration_date from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (1,2,3,4) THEN 'PENDING' when wf.previous_group_id IN (2,3,4) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (2,4,3) THEN 'PENDING' when wf.previous_group_id IN (2,3,5) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and application_type_id IN (7) and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and application_type_id IN (7) and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (4) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status, wf.id, work_flow_status_id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id from main.work_flow_audit as wf INNER JOIN ( select request_id as lat_request_id, hp_profile_id as lat_hp_profile_id from main.work_flow order by hp_profile_id desc ) latest_wf on request_id = lat_request_id order by lat_hp_profile_id desc, wf.id asc ) calculate INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id ";

    public static final String FETCH_CARD_DETAILS_COUNT_QUERY = "select count(*) from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (1,2,3,4) THEN 'PENDING' when wf.previous_group_id IN (2,3,4) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (2,4,3) THEN 'PENDING' when wf.previous_group_id IN (2,3,5) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and application_type_id IN (7) and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and application_type_id IN (7) and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (4) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status, wf.id, work_flow_status_id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id from main.work_flow_audit as wf INNER JOIN ( select request_id as lat_request_id, hp_profile_id as lat_hp_profile_id from main.work_flow order by hp_profile_id desc ) latest_wf on request_id = lat_request_id order by lat_hp_profile_id desc, wf.id asc ) calculate INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id ";

    public static final String FETCH_TRACK_DETAILS_QUERY = "select doctor_status, smc_status, college_dean_status, college_registrar_status, nmc_status, nbe_status, calculate.hp_profile_id, calculate.request_id, rd.registration_no, rd.created_at,stmc.name, hp.full_name, application_type_id, (SELECT a.name FROM main.application_type a WHERE a.id=application_type_id) as application_type_name, DATE_PART('day', (now() - TO_TIMESTAMP(rd.created_at, 'YYYY-MM-DD HH24:MI:SS'))) as pendency, work_flow_status_id, hp.gender, hp.email_id, hp.mobile_number, hp.nmr_id, rd.registration_date from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (1,2,3,4) THEN 'PENDING' when wf.previous_group_id IN (2,3,4) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (2,4,3) THEN 'PENDING' when wf.previous_group_id IN (2,3,5) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and application_type_id IN (7) and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and application_type_id IN (7) and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (4) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status , wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id, wf.work_flow_status_id from main.work_flow_audit as wf order by wf.hp_profile_id desc, wf.id asc ) calculate INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = rd.request_id where calculate.hp_profile_id IS NOT NULL and current_status = 1 ";
    public static final String FETCH_TRACK_DETAILS_COUNT_QUERY = "select count(*) from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (1,2,3,4) THEN 'PENDING' when wf.previous_group_id IN (2,3,4) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 and application_type_id IN (1,8) THEN 'APPROVED' when wf.previous_group_id = 1 and application_type_id IN (1,8) and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and application_type_id IN (1,8) and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and application_type_id IN (1,8) and wf.action_id IN (2,4,3) THEN 'PENDING' when wf.previous_group_id IN (2,3,5) and application_type_id IN (1,8) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and application_type_id IN (1,8) and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and application_type_id IN (7) and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and application_type_id IN (7) and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and application_type_id IN (7) and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (4) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status , wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id, wf.work_flow_status_id from main.work_flow_audit as wf order by wf.hp_profile_id desc, wf.id asc ) calculate INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = rd.request_id where calculate.hp_profile_id IS NOT NULL and current_status = 1 ";
    public static final String INVALID_USER_SUB_TYPE = "Invalid User sub-type. Expected: College, College Dean or College Registrar";

    public static final String INVALID_USER_TYPE = "Invalid User type. Expected: Health Professional, College, State Medical Council or National Medical Council";

    public static final String INVALID_GROUP = "Invalid Group. Expected: Health Professional, State Medical Council, National Medical Council, College Dean, College Registrar or College Admin";

    public static final String INVALID_DASHBOARD_GROUP = "Invalid Group. Expected: State Medical Council, National Medical Council, NBE, College Dean, College Registrar or College Admin";
    public static final String INVALID_WORK_FLOW_STATUS = "Invalid Workflow Status. Expected: Pending, Approved, Query Raised, Rejected, Suspended or Blacklisted";
    public static final String INVALID_APPLICATION_TYPE = "Invalid Application Type. Expected: HP Registration, HP Modification, Temporary Suspension, Permanent Suspension, Activate License, College Registration ";
    public static final String STATE_MEDICAL_COUNCIL_ID = "state_medical_council_id";

    public static final String REGISTRATION_DETAILS_ID = "registration_details_id";

    public static final String REGISTRATION_NUMBER = "registrationNumber";

    public static final String SMC_NAME = "smcName";
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
    public static final String MESSAGE_SENDER = "National Medical Council";
    public static final String DEFAULT_COUNTRY_AADHAR = "India";
    public static final int DEFAULT_ADDRESS_TYPE_AADHAR = 4;
    public static final String INDIA = "India";
    public static final String INTERNATIONAL = "International";
    public static final String SALUTATION_DR = "Dr.";
    public static final String COLLEGE_ID_IN_LOWER_CASE = "collegeid";

    public static final String COLLEGE_NAME_IN_LOWER_CASE = "collegename";

    public static final String COUNCIL_NAME_IN_LOWER_CASE = "councilname";

    public static final String SEARCH_IN_LOWER_CASE = "search";

    public static final String NAME_IN_LOWER_CASE = "name";

    public static final String APPLICATION_TYPE_ID_IN_LOWER_CASE = "applicationtypeid";

    public static final String REGISTRATION_NUMBER_IN_LOWER_CASE = "registrationnumber";

    public static final String GENDER_IN_LOWER_CASE = "gender";
    public static final String EMAIL_ID_IN_LOWER_CASE = "emailid";
    public static final String MOBILE_NUMBER_IN_LOWER_CASE = "mobilenumber";

    public static final String YEAR_OF_REGISTRATION_IN_LOWER_CASE = "yearofregistration";

    public static final String NMR_ID_IN_LOWER_CASE = "nmrid";

    public static final String SMC_ID_IN_LOWER_CASE = "smcid";

    public static final String WORK_FLOW_STATUS_IN_LOWER_CASE = "workflowstatus";

    public static final String WORK_FLOW_STATUS_ID_IN_LOWER_CASE = "workflowstatusid";

    public static final String APPLICANT_FULL_NAME_IN_LOWER_CASE = "applicantfullname";


    public static final String REACTIVATION_RECORDS = """
            WITH reactivate AS(
            SELECT hp.id ,hp.registration_id ,wf.request_id,hp.full_name , wf.created_at, wf.start_date, hp.gender, hp.email_id, hp.mobile_number,
            (
            SELECT b.name FROM main.application_type b WHERE b.id = (SELECT wol.application_type_id FROM main.work_flow wol
            WHERE wol.hp_profile_id = (
            SELECT hpPr.id FROM main.hp_profile hpPr where hpPr.registration_id = hp.registration_id  ORDER BY id DESC LIMIT 1 OFFSET 1)
            )) AS suspension_type,wf.remarks
            FROM main.work_flow wf  INNER JOIN main.hp_profile hp ON wf.hp_profile_id=hp.id
            JOIN main.application_type a ON wf.application_type_id=a.id WHERE wf.application_type_id= """ + HP_ACTIVATE_LICENSE.getId();
    public static final String FETCH_REACTIVATION_RECORDS = REACTIVATION_RECORDS +
            """
                    )                        
                    SELECT *  FROM reactivate r
                    WHERE 1=1 """;

    public static final String FETCH_COUNT_OF_REACTIVATION_RECORDS = REACTIVATION_RECORDS +
            """
                    )                        
                    SELECT COUNT(*)  FROM reactivate r
                    WHERE 1=1 """;

    public static final String NOT_NULL_ERROR_MSG = "The {0} is mandatory.";
    public static final String NOT_BLANK_ERROR_MSG = "The {0} should not be blank.";
    public static final String INPUT_VALIDATION_ERROR_CODE = "ABDM-NMR-400";
    public static final String INPUT_VALIDATION_INTERNAL_ERROR_CODE = "ABDM-NMR-401";
    public static final String INVALID_INPUT_ERROR_MSG = "Invalid input";
    public static final String INVALID_PINCODE = "pincode should be of max 6 digit";

    public static final String FETCH_WORK_PROFILE_RECORDS_BY_HP_ID = """
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id, 
            broad_speciality_id, state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at, 
            updated_at, request_id, facility_type_id, organization_type, registration_no FROM work_profile where hp_profile_id =:""" + HP_PROFILE_ID;

    public static final String FETCH_WORK_PROFILE_RECORDS_BY_REG_NO = """
            SELECT address, facility_id, is_user_currently_working, pincode, proof_of_work_attachment, url, district_id, user_id, 
            broad_speciality_id, state_id, work_nature_id, work_status_id, hp_profile_id, work_organization, id, created_at, 
            updated_at, request_id, facility_type_id, organization_type, registration_no FROM work_profile where registration_no =:""" + REGISTRATION_NUMBER;

    public static final int MAX_DATA_SIZE = 500;
    public static final String DEFAULT_SORT_ORDER = "ASC";
    public static final String NO_DATA_FOUND = "No data found";
    public static final String USER_NAME_ALREADY_EXISTS = "Username already exist";
    public static final String SMS_AND_EMAIL_RESET_PASSWORD_MESSAGE_PROPERTIES_KEY = "sms-email-reset";
    public static final String TYPE_NOT_NULL = "Type cannot be null";
    public static final String USER_REQUEST_MAPPING = "/user";

    public static final String ACCESS_FORBIDDEN = "Access Forbidden.";

    public static final String COMMA_SEPARATOR = ",";

    public static final String QUALIFICATION_DETAILS_NULL_ERROR = "The field 'qualificationDetails' is mandatory. ";

    public static final String QUALIFICATION_DETAILS_EMPTY_ERROR = "Please provide at-least one qualification to be added. ";

    public static final String PROOFS_NULL_ERROR = "The field 'proofs' is mandatory. ";

    public static final String PROOFS_EMPTY_ERROR = "Please provide at-least one proof document. ";

    public static final String MISSING_PROOFS_ERROR = "Please provide proofs for all the qualification details provided. ";

    public static final String EXCESS_PROOFS_ERROR = "Please remove excess proofs to sync with the qualification details provided. ";

    public static final String QUALIFICATION_DETAILS_LIMIT_EXCEEDED = "Please provide less than or equal to 6 qualifications at a time.";

    public static final String WORK_PROFILE_DETAILS_NULL_ERROR = "The field 'currentWorkDetails' is mandatory. ";

    public static final String WORK_PROFILE_DETAILS_EMPTY_ERROR = "Please provide at-least one work profile detail to be added. ";

    public static final String MISSING_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR = "Please provide proofs for all the work profile details provided. ";

    public static final String EXCESS_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR = "Please remove excess proofs to sync with the work profile details provided. ";

    public static final String WORK_PROFILE_DETAILS_LIMIT_EXCEEDED = "Please provide less than or equal to 6 work profiles at a time.";

    public static final String INVALID_SEARCH_CRITERIA_FOR_TRACK_STATUS_AND_APPLICATION = "Invalid Search Criteria. Expected: registrationnumber or smcid ";

    public static final String INVALID_SEARCH_CRITERIA_FOR_REACTIVATE_LICENSE = "Invalid Search Criteria. Expected: search";

    public static final String INVALID_SEARCH_CRITERIA_FOR_GET_CARD_DETAIL = "Invalid Search Criteria. Expected: name, nmrid, smcid or search";

    public static final String INVALID_SEARCH_CRITERIA_FOR_POST_CARD_DETAIL = "Invalid Search Criteria. Expected: collegeid, name, nmrid, smcid, workflowstatus or search";

    public static final String INVALID_SEARCH_CRITERIA_FOR_COLLEGE_APPLICATIONS = "Invalid Search Criteria. Expected: collegeid, collegename, councilname or search";

    public static final String MISSING_SEARCH_VALUE = "Please Enter a search value.";

    public static final String NO_MATCHING_REGISTRATION_DETAILS_FOUND = "No matching registration details found for the given hp_profile_id";
    public static final double FUZZY_MATCH_LIMIT = 75;
    public static final String FUZZY_PARAMETER_NAME = "Name";
    public static final String FUZZY_PARAMETER_GENDER = "Gender";
    public static final String FUZZY_PARAMETER_DOB = "DOB";

    public static final String USER_ID_COLUMN = "user_id";

    public static final String PRIMARY_CONTACT_NO_COLUMN = "primary_contact_no";

    public static final String EMAIL_ID_COLUMN = "email_id";

    public static final String FULL_NAME_COLUMN = "full_name";

    public static final String NAME_COLUMN = "name";

    public static final String HP_PROFILE_ID_COLUMN = "hp_profile_id";

    public static final String REGISTRATION_NO_COLUMN = "registration_no";

    public static final String USER_ALREADY_EXISTS_EXCEPTION = "Your account already exists. Please login with your credentials! ";

    public static final String NO_MATCHING_WORK_PROFILE_DETAILS_FOUND = "No matching work profile details found for the given hp_profile_id";
}
