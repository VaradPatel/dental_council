package in.gov.abdm.nmr.security.common;

import in.gov.abdm.nmr.controller.CollegeController;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class ProtectedPaths {



    private ProtectedPaths() {
    }

    public static final String PATH_LOGIN = "/user/login";
    public static final String PATH_LOGIN_INTERNAL = "/user/login-internal";
    public static final String PATH_REFRESH_TOKEN = "/user/refreshToken";

    public static final String PATH_UPDATE_COLLEGE = "/college/{collegeId}";
    public static final String PATH_REGISTER_COLLEGE_REGISTRAR = "/college/{collegeId}/registrar";
    public static final String PATH_REGISTER_COLLEGE_DEAN = "/college/{collegeId}/dean";

    public static final String PATH_COLLEGE_PROFILE = "/college/{collegeId}";
    public static final String PATH_COLLEGE_REGISTRAR_PROFILE = "/college/{collegeId}/registrar/{registrarId}";
    public static final String PATH_COLLEGE_DEAN_PROFILE = "/college/{collegeId}/dean/{deanId}";


    public static final String PATH_USER_ROOT = "/user";
    public static final String PATH_USER_SMSNOTIFICATIONENABLED = "/enable-sms-notification";
    public static final String PATH_USER_EMAILNOTIFICATIONENABLED = "/enable-email-notification";
    public static final String PATH_ACTION_ROOT = "/health-professional/applications/";

    public static final String PATH_SMC_PROFILE = "/smc/user/{id}";
    public static final String PATH_NMC_PROFILE = "/nmc/user/{id}";
    public static final String PATH_NBE_PROFILE = "/nbe/user/{id}";

    public static final String PATH_USER_NOTIFICATION_ENABLED = "/user/enable-notification";

    public static final String PATH_DASHBOARD_FETCH_DETAILS = "/card-detail";

    public static final String PATH_DASHBOARD_ROOT = "/dashboards";

    public static final String PATH_DASHBOARD_CARD_COUNT = "/cards";

    public static final String PATH_HEALTH_PROFESSIONAL_ROOT = "/health-professional";

    public static final String PATH_HEALTH_PROFESSIONAL_APPLICATIONS = PATH_HEALTH_PROFESSIONAL_ROOT + "/{healthProfessionalId}/applications";

    public static final String APPLICATION_REQUEST_URL = "/health-professional/applications";
    public static final String SUSPENSION_REQUEST_URL = APPLICATION_REQUEST_URL + "/suspend";
    public static final String REACTIVATE_REQUEST_URL = APPLICATION_REQUEST_URL + "/re-activate";
    public static final String CHANGE_PASSWORD = "/user/change-password";
    public static final String HEALTH_PROFESSIONAL_ACTION = APPLICATION_REQUEST_URL + "/status";
    public static final String COLLEGES_ACTION = NMRConstants.COLLEGES_ACTION;
    public static final String USER_NMC_CREATE_USER = "/user";
    public static final String USER_UNLOCK_URL = "/user/{userId}/unlock";
    public static final String USER_DEACTIVATE_URL = "/user/{userId}/deactivate";
    public static final String DE_LINK_FACILITY = "/health-professional/work-profile/facility";
    public static final String SPECIALITIES = "/specialities";
    public static final String COUNTRIES = "/countries";
    public static final String STATES = "/countries/{country_id}/states";
    public static final String DISTRICTS = "/countries/states/{state_id}/districts";
    public static final String SUB_DISTRICTS = "/countries/states/districts/{district_id}/sub_districts";
    public static final String CITIES = "/countries/states/districts/sub-districts/{sub_district_id}/cities";
    public static final String LANGUAGES = "/languages";
    public static final String COURSES = "/courses";
    public static final String RENEW_TYPES = "/renewation-types";
    public static final String FACILITY_TYPES = "/facility-types";
    public static final String COLLEGE = "/college";
    public static final String UNIVERSITY = "/university";
    public static final String QUERY_SUGGESTIONS = "/query-suggestions";
    public static final String APPLICATION_DETAILS = "/applications/{requestId}";
    public static final String E_SIGN = "/e-signature";
    public static final String PATH_FACILITY_SEARCH = "/search";
    public static final String CREATE_PROFESSIONAL_PERSONAL = "health-professional/personal";
    public static final String UPDATE_PROFESSIONAL_PERSONAL = "health-professional/{healthProfessionalId}/personal";
    public static final String GET_PROFESSIONAL_PERSONAL = "health-professional/{healthProfessionalId}/personal";
    public static final String UPDATE_REGISTRATION = "health-professional/{healthProfessionalId}/registration";
    public static final String GET_REGISTRATION = "health-professional/{healthProfessionalId}/registration";
    public static final String UPDATE_WORK_PROFILE = "health-professional/{healthProfessionalId}/work-profile";
    public static final String GET_WORK_PROFILE = "health-professional/{healthProfessionalId}/work-profile";
    public static final String ADDITIONAL_QUALIFICATION = "/health-professional/{healthProfessionalId}/qualifications";
    public static final String PROFILE_PICTURE = "/health-professional/{healthProfessionalId}/profile-picture";
    public static final String HEALTH_PROFESSIONAL_REGISTER = "health-professional/register";
    public static final String RAISE_QUERY = "/health-professional/queries";
    public static final String GET_QUERIES = "health-professional/{healthProfessionalId}/queries";
    public static final String UPDATE_PERSONAL_EMAIL = "health-professional/{healthProfessionalId}/personal";
    public static final String GET_EMAIl_LINK = "health-professional/{healthProfessionalId}/email";
    public static final String GET_USER = "/user";
    public static final String GET_USER_ACCOUNT = "/user-accounts";

    public static AntPathRequestMatcher[] getProtectedPathsMatchers() {
        List<AntPathRequestMatcher> protectedPaths = new ArrayList<>();
        protectedPaths.add(new AntPathRequestMatcher(PATH_REFRESH_TOKEN));

        protectedPaths.add(new AntPathRequestMatcher(PATH_UPDATE_COLLEGE, HttpMethod.PUT.name()));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_REGISTRAR));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_DEAN));

        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_PROFILE, HttpMethod.GET.name()));
        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_DEAN_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_REGISTRAR_PROFILE));

        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_NOTIFICATION_ENABLED));

        protectedPaths.add(new AntPathRequestMatcher(PATH_SMC_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_NMC_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_NBE_PROFILE));

        protectedPaths.add(new AntPathRequestMatcher(PATH_DASHBOARD_ROOT + PATH_DASHBOARD_FETCH_DETAILS));
        protectedPaths.add(new AntPathRequestMatcher(PATH_DASHBOARD_ROOT + PATH_DASHBOARD_CARD_COUNT));

        protectedPaths.add(new AntPathRequestMatcher(PATH_HEALTH_PROFESSIONAL_ROOT + PATH_HEALTH_PROFESSIONAL_APPLICATIONS));
        protectedPaths.add(new AntPathRequestMatcher(SUSPENSION_REQUEST_URL));
        protectedPaths.add(new AntPathRequestMatcher(REACTIVATE_REQUEST_URL));
        protectedPaths.add(new AntPathRequestMatcher(CHANGE_PASSWORD));
        
        protectedPaths.add(new AntPathRequestMatcher(HEALTH_PROFESSIONAL_ACTION));
        protectedPaths.add(new AntPathRequestMatcher(COLLEGES_ACTION));
        
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES, HttpMethod.GET.name()));
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES, HttpMethod.POST.name()));
        
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGE_ID, HttpMethod.GET.name()));
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGE_ID, HttpMethod.PUT.name()));
        
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES_VERIFIERS_DESIGNATION, HttpMethod.GET.name()));
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES_COLLEGE_ID_VERIFIERS, HttpMethod.POST.name()));
        
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES_COLLEGE_ID_VERIFIERS_VERIFIER_ID, HttpMethod.PUT.name()));
        protectedPaths.add(new AntPathRequestMatcher(CollegeController.COLLEGES_COLLEGE_ID_VERIFIERS_VERIFIER_ID, HttpMethod.GET.name()));
        
        protectedPaths.add(new AntPathRequestMatcher(USER_NMC_CREATE_USER));
        protectedPaths.add(new AntPathRequestMatcher(DE_LINK_FACILITY,HttpMethod.DELETE.name()));
        protectedPaths.add(new AntPathRequestMatcher(USER_UNLOCK_URL));
        protectedPaths.add(new AntPathRequestMatcher(USER_DEACTIVATE_URL));

        protectedPaths.add(new AntPathRequestMatcher(SPECIALITIES));
        protectedPaths.add(new AntPathRequestMatcher(COUNTRIES));
        protectedPaths.add(new AntPathRequestMatcher(STATES));
        protectedPaths.add(new AntPathRequestMatcher(DISTRICTS));
        protectedPaths.add(new AntPathRequestMatcher(SUB_DISTRICTS));
        protectedPaths.add(new AntPathRequestMatcher(CITIES));
        protectedPaths.add(new AntPathRequestMatcher(COURSES));
        protectedPaths.add(new AntPathRequestMatcher(LANGUAGES));
        protectedPaths.add(new AntPathRequestMatcher(RENEW_TYPES));
        protectedPaths.add(new AntPathRequestMatcher(FACILITY_TYPES));
        protectedPaths.add(new AntPathRequestMatcher(COLLEGE));
        protectedPaths.add(new AntPathRequestMatcher(UNIVERSITY));
        protectedPaths.add(new AntPathRequestMatcher(QUERY_SUGGESTIONS));
        protectedPaths.add(new AntPathRequestMatcher(QUERY_SUGGESTIONS));
        protectedPaths.add(new AntPathRequestMatcher(APPLICATION_REQUEST_URL));
        protectedPaths.add(new AntPathRequestMatcher(APPLICATION_DETAILS));
        protectedPaths.add(new AntPathRequestMatcher(E_SIGN));
        protectedPaths.add(new AntPathRequestMatcher(PATH_FACILITY_SEARCH));
        protectedPaths.add(new AntPathRequestMatcher(CREATE_PROFESSIONAL_PERSONAL));
        protectedPaths.add(new AntPathRequestMatcher(UPDATE_PROFESSIONAL_PERSONAL));
        protectedPaths.add(new AntPathRequestMatcher(GET_PROFESSIONAL_PERSONAL));
        protectedPaths.add(new AntPathRequestMatcher(UPDATE_REGISTRATION));
        protectedPaths.add(new AntPathRequestMatcher(GET_REGISTRATION));
        protectedPaths.add(new AntPathRequestMatcher(UPDATE_WORK_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(ADDITIONAL_QUALIFICATION));
        protectedPaths.add(new AntPathRequestMatcher(PROFILE_PICTURE));
        protectedPaths.add(new AntPathRequestMatcher(HEALTH_PROFESSIONAL_REGISTER));
        protectedPaths.add(new AntPathRequestMatcher(RAISE_QUERY));
        protectedPaths.add(new AntPathRequestMatcher(GET_QUERIES));
        protectedPaths.add(new AntPathRequestMatcher(UPDATE_PERSONAL_EMAIL));
        protectedPaths.add(new AntPathRequestMatcher(GET_EMAIl_LINK));
        protectedPaths.add(new AntPathRequestMatcher(GET_USER));

        return protectedPaths.toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN, HttpMethod.POST.name());
    }
    
    public static AntPathRequestMatcher getInternalLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN_INTERNAL, HttpMethod.POST.name());
    }
}
