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
    
    public static final String HEALTH_PROFESSIONAL_ACTION = NMRConstants.HEALTH_PROFESSIONAL_ACTION;
    public static final String COLLEGES_ACTION = NMRConstants.COLLEGES_ACTION;
    
    public static final String USER_NMC_CREATE_USER = "/user";

    public static final String USER_UNLOCK_URL = "/user/{userId}/unlock";
    public static final String USER_DEACTIVATE_URL = "/user/{userId}/deactivate";
    public static final String DE_LINK_FACILITY = "/health-professional/work-profile/facility";
    public static final String HP_REGISTER = "/health-professional/register";
    public static final String RAISE_QUERY = "/health-professional/queries";
    public static final String ADDITIONAL_QUALIFICATION = "/health-professional/{healthProfessionalId}/qualifications";
    public static final String HEALTH_PROFESSIONAL_PERSONAL = "/health-professional/{healthProfessionalId}/personal";

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
        protectedPaths.add(new AntPathRequestMatcher(APPLICATION_REQUEST_URL));
        protectedPaths.add(new AntPathRequestMatcher(HP_REGISTER));
        protectedPaths.add(new AntPathRequestMatcher(RAISE_QUERY));
        protectedPaths.add(new AntPathRequestMatcher(ADDITIONAL_QUALIFICATION));
        protectedPaths.add(new AntPathRequestMatcher(HEALTH_PROFESSIONAL_PERSONAL));

        return protectedPaths.toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN, HttpMethod.POST.name());
    }
    
    public static AntPathRequestMatcher getInternalLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN_INTERNAL, HttpMethod.POST.name());
    }
}
