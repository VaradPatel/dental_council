package in.gov.abdm.nmr.util;


import lombok.experimental.UtilityClass;

/**
 * This class holds all the constants associated with NMR application
 */


@UtilityClass
public class NMRConstants {

    public static final String DASHBOARD_REQUEST_URL="/dashboard";

    public static final String FETCH_COUNT_ON_CARD_URL = "/cardCount";

    public static final String FETCH_SPECIFIC_DETAILS_URL = "/fetchSpecificDetails";

    public static final String FETCH_DETAILS_BY_REG_NO_URL = "/fetchDetailsByRegNo";

    public static final String GENERATE_OTP = "/generateOtp";
    public static final String GENERATE_AADHAR_OTP = "/sendAadhaarOtp";
    public static final String VALIDATE_OTP = "/validateOtp";
    public static final String VALIDATE_AADHAR_OTP = "/verifyAadhaarOtp";
    public static final String AADHAR_SERVICE_SEND_OTP = "/api/v3/aadhaar/sendOtp";
    public static final String AADHAR_SERVICE_VERIFY_OTP = "/api/v3/aadhaar/verifyOtp";
    public static final String NOTIFICATION_SERVICE_SEND_MESSAGE = "/internal/v3/notification/message";
    public static final String NOTIFICATION_DB_SERVICE_GET_TEMPLATE = "/internal/v3/notification/template/id/{id}";
    public static final String RESET_PASSWORD = "/resetPassword";
    public static final String CHANGE_PASSWORD = "/changePassword";
    public static final String RAISE_QUERY = "/raiseQuery";
    public static final String GET_QUERIES = "/queries/hpProfileId/{hpProfileId}";

    public static final String ACTION_REQUEST_URL="/action";

    public static final String INITIATE_WORK_FLOW_URL="/initiateWorkFlow";
    public static final String INITIATE_COLLEGE_WORK_FLOW_URL="/initiateCollegeWorkFlow";

    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String NOTIFICATION_DB_SERVICE = "notification-db";
    public static final String AADHAAR_SERVICE = "aadhaar";
    public static final String CLOSED_STATUS = "closed";
    public static final String OPEN_STATUS = "open";

    public static final String GLOBAL_NOTIFICATION_ENDPOINT = "${global.notification.endpoint}";
     public static final String GLOBAL_AADHAAR_ENDPOINT = "${global.aadhaar.endpoint}";

    public static final String OTP_GENERATION_EXCEEDED = "OTP Generation Attempts Exceeded";
    public static final String OTP_ATTEMPTS_EXCEEDED = "OTP Attempts Exceeded";
    public static final String NO_SUCH_OTP_TYPE = "No such OTP Type";
    public static final String OTP_EMAIL_SUBJECT = "NMR : Email Verification OTP";
    public static final String INFO_EMAIL_SUBJECT = "NMR : Status Changed";
    public static final String OTP_CONTENT_TYPE = "otp";
    public static final String OTP_MESSAGES_PROPERTIES_KEY = "otp";
    public static final String INFO_CONTENT_TYPE = "info";
    public static final String COLLEGE_PREFIX_TO_GET_WORKFLOW_STATUS = "college-";
    public static final String CONTACT_NOT_NULL = "Contact cannot be null or empty";
    public static final String AADHAR_NOT_NULL = "AADHAR Number cannot be null or empty";
    public static final String TYPE_NOT_NULL = "Type cannot be null or empty";
    public static final String TRANSACTION_ID_NOT_NULL = "Transaction Id cannot be null or empty";
    public static final String OTP_NOT_NULL = "OTP cannot be null or empty";
    public static final String SUCCESS_RESPONSE = "Success";
    public static final String FAILURE_RESPONSE = "Fail";
    public static final String SENT_RESPONSE = "sent";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String WORKFLOW_STATUS_NOT_FOUND = "Workflow status not found";
    public static final String TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES = "Template id not found in properties";
    public static final String TEMPLATE_NOT_FOUND = "Template not found";
    public static final String OLD_PASSWORD_NOT_MATCHING = "Old password not matching";
    public static final String PROBLEM_OCCURRED = "Problem Occurred";
    public static final String USERNAME_NOT_NULL = "Username cannot be null or empty";
    public static final String PASSWORD_NOT_NULL = "Password cannot be null or empty";
    public static final String OTP_INVALID = "Invalid OTP";
    public static final String OTP_NOT_FOUND = "OTP Not Found";
    public static final String OTP_EXPIRED = "OTP Expired";
    public static final String TEMPLATE_ID = "templateId";
    public static final String SUBJECT = "subject";
    public static final String CONTENT = "content";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String MOBILE = "mobile";
    public static final String EMAIL_ID = "emailId";
    public static final int OTP_GENERATION_MAX_ATTEMPTS = 5;
    public static final int OTP_MAX_ATTEMPTS = 3;

    public static final String TOTAL_HP_REGISTRATION_REQUESTS = "Total HP Registration Requests";
    public static final String TOTAL_HP_MODIFICATION_REQUESTS = "Total HP Modification Requests";

    public static final String TOTAL_TEMPORARY_SUSPENSION_REQUESTS="Total Temporary Suspension Requests";

    public static final String TOTAL_PERMANENT_SUSPENSION_REQUESTS="Total Permanent Suspension Requests";

    public static final String TOTAL_ACTIVATE_LICENSE_REQUESTS="Total Activate License Requests";

    public static final String TOTAL_COLLEGE_REGISTRATION_REQUESTS = "Total College Registration Requests";

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

    public static final String APPLICATION_TYPE_ID="applicationTypeId";
    public static final String APPLICATION_TYPE_NAME="applicationTypeName";

    public static final String GROUP_ID="groupId";

    public static final String GROUP_NAME="groupName";

    public static final String WORK_FLOW_STATUS="workFlowStatus";
    public static final String FETCH_STATUS_WISE_COUNT_BY_GROUP_AND_APPLICATION_TYPE_QUERY="SELECT ws.name as name, COUNT(w) as count " +
            "FROM work_flow w JOIN work_flow_status ws ON w.work_flow_status_id = ws.id " +
            "WHERE w.application_type_id = :"+APPLICATION_TYPE_ID+" AND  w.current_group_id = :"+GROUP_ID+" " +
            "OR ( w.previous_group_id = :"+GROUP_ID+" AND w.action_id IN ( 3,5 ) ) " +
            "GROUP BY ws.name " +
            "UNION " +
            "SELECT ws.name as status, COUNT(wa) as count FROM work_flow_audit wa " +
            "JOIN work_flow_status ws ON wa.work_flow_status_id = ws.id " +
            "WHERE wa.application_type_id = 1 AND wa.previous_group_id = :"+GROUP_ID +" "+
            "AND wa.action_id = 4"+
            "GROUP BY ws.name";

    public static final String FETCH_DETAILS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow w JOIN registration_details rd ON w.hp_profile_id=rd.hp_profile_id " +
            "JOIN hp_profile hp ON w.hp_profile_id = hp.id " +
            "JOIN application_type a ON w.application_type_id = a.id " +
            "JOIN \"group\" g ON w.previous_group_id=g.id " +
            "JOIN work_flow_status ws ON w.work_flow_status_id= ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :"+GROUP_NAME+ " " +
            "AND a.name = :"+APPLICATION_TYPE_NAME+" " +
            "AND ws.name = :"+WORK_FLOW_STATUS+" ";

    public static final String FETCH_DETAILS_WITH_PENDING_STATUS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow w JOIN registration_details rd ON w.hp_profile_id = rd.hp_profile_id " +
            "JOIN hp_profile hp ON w.hp_profile_id = hp.id " +
            "JOIN application_type a ON w.application_type_id = a.id " +
            "JOIN \"group\" g ON w.current_group_id = g.id " +
            "JOIN work_flow_status ws ON w.work_flow_status_id = ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :"+GROUP_NAME+ " " +
            "AND a.name = :"+APPLICATION_TYPE_NAME+" " +
            "AND ws.name = :"+WORK_FLOW_STATUS+" ";

    public static final String FETCH_DETAILS_WITH_APPROVED_STATUS_FOR_LISTING_QUERY = "SELECT rd.registration_no as registrationNo, hp.full_name as nameOfApplicant, smc.name as nameOfStateCouncil, rd.registration_date as dateOfSubmission, g.name as groupName, ws.name as workFlowStatus " +
            "FROM work_flow_audit wa JOIN registration_details rd ON w.hp_profile_id=rd.hp_profile_id " +
            "JOIN hp_profile hp ON wa.hp_profile_id = hp.id " +
            "JOIN application_type a ON wa.application_type_id = a.id " +
            "JOIN \"group\" g ON wa.previous_group_id=g.id " +
            "JOIN work_flow_status ws ON wa.work_flow_status_id= ws.id " +
            "JOIN state_medical_council smc ON rd.state_medical_council_id = smc.id " +
            "WHERE g.name = :"+GROUP_NAME+ " " +
            "AND a.name = :"+APPLICATION_TYPE_NAME+" " +
            "AND ws.name = :"+WORK_FLOW_STATUS+" ";

    public static final String FETCH_DETAILS_BY_REG_NO_QUERY = "SELECT hvs.registrationDetails.registrationNo as registrationNo, hvs.hpProfile.fullName as nameOfApplicant, hvs.registrationDetails.stateMedicalCouncil.name as nameOfStateCouncil, hvs.registrationDetails.registrationDate as dateOfSubmission, hvs.verifiedBy.userType.name as verifiedByUserType, hvs.verifiedBy.userSubType.name as verifiedByUserSubType, hvs.hpProfile.hpProfileStatus.name as hpProfileStatus "+
            "FROM HpVerificationStatus hvs "+
            "WHERE hvs.registrationDetails.registrationNo=:registrationNumber"+
            "AND hvs.registrationDetails.stateMedicalCouncil.name =:smcName "+
            "AND hvs.verifiedBy.userType.name =:userType "+
            "AND hvs.verifiedBy.userSubType.name =:userSubType ";

    public static final String FETCH_DETAILS_FOR_NMC_BY_REG_NO_QUERY = "SELECT hvs.registrationDetails.registrationNo as registrationNo, hvs.hpProfile.fullName as nameOfApplicant, hvs.registrationDetails.stateMedicalCouncil.name as nameOfStateCouncil, hvs.registrationDetails.registrationDate as dateOfSubmission, hvs.verifiedBy.userType.name as verifiedByUserType, hvs.verifiedBy.userSubType.name as verifiedByUserSubType, hvs.hpProfile.hpProfileStatus.name as hpProfileStatus "+
            "FROM HpVerificationStatus hvs "+
            "WHERE hvs.registrationDetails.registrationNo=:registrationNumber"+
            "AND hvs.verifiedBy.userType.name =:userType "+
            "AND hvs.verifiedBy.userSubType.name =:userSubType ";

    public static final String INVALID_USER_SUB_TYPE="Invalid User sub-type. Expected: College, College Dean or College Registrar";

    public static final String INVALID_USER_TYPE="Invalid User type. Expected: Health Professional, College, State Medical Council or National Medical Council";

    public static final String INVALID_GROUP="Invalid Group. Expected: Health Professional, State Medical Council, National Medical Council, College Dean, College Registrar or College Admin";
    public static final String INVALID_WORK_FLOW_STATUS="Invalid Workflow Status. Expected: Pending, Approved, Query Raised, Rejected, Suspended or Blacklisted";
    public static final String INVALID_APPLICATION_TYPE="Invalid Application Type. Expected: HP Registration, HP Modification, Temporary Suspension, Permanent Suspension, Activate License, College Registration ";
    public static final String STATE_MEDICAL_COUNCIL_ID="state_medical_council_id";

    public static final String REGISTRATION_DETAILS_ID = "registration_details_id";

    public static final String REGISTRATION_NUMBER = "registrationNumber";

    public static final String SMC_NAME = "smcName";
    public static final String SUCCESS="Success";
    public static final String DCS_INTEGRATOR_NAME="NMR";
    public static final String E_SIGN_SUCCESS_STATUS="success";
    public static final String E_SIGN_FAILURE_STATUS="failure";
    public static final String DSC_SERVICE = "dsc";
    public static final String DSC_SERVICE_ENDPOINT = "${dsc.endpoint.url}";
    public static final String GEN_ESP_REQUEST_URL = "/digiSign/genEspRequest";
    public static final String VERIFY_ESP_REQUEST_URL = "/digiSign/pdf/{tansactionId}";
}
