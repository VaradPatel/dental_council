package in.gov.abdm.nmr.security.common;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class ProtectedPaths {

    private ProtectedPaths() {
    }

    public static final String PATH_LOGIN = "/user/login";
    public static final String PATH_REFRESH_TOKEN = "/user/refreshToken";
    
    public static final String PATH_UPDATE_COLLEGE = "/college";
    public static final String PATH_REGISTER_COLLEGE_REGISTRAR = "/college/{collegeId}/registrar";
    public static final String PATH_REGISTER_COLLEGE_DEAN = "/college/{collegeId}/dean";
    
    public static final String PATH_COLLEGE_PROFILE = "/college/{id}";
    public static final String PATH_COLLEGE_REGISTRAR_PROFILE = "/college/registrar/{id}";
    public static final String PATH_COLLEGE_DEAN_PROFILE = "/college/dean/{id}";


    public static final String PATH_USER_ROOT = "/user";
    public static final String PATH_USER_SMSNOTIFICATIONENABLED = "/enable-sms-notification";
    public static final String PATH_USER_EMAILNOTIFICATIONENABLED = "/enable-email-notification";
    
    public static final String PATH_SMC_PROFILE = "/smc/user/{id}";
    public static final String PATH_NMC_PROFILE = "/nmc/user/{id}";
    public static final String PATH_NBE_PROFILE = "/nbe/user/{id}";

    public static final String PATH_USER_NOTIFICATION_ENABLED = "/enable-notification";

    public static final String PATH_DASHBOARD_FETCH_DETAILS = "/card-detail";

    public static final String PATH_DASHBOARD_ROOT = "/dashboards";

    public static final String PATH_DASHBOARD_CARD_COUNT="/cards";

    public static final String PATH_HEALTH_PROFESSIONAL_ROOT = "/health-professional";

    public static final String PATH_HEALTH_PROFESSIONAL_APPLICATIONS = "/applications";

    public static AntPathRequestMatcher[] getProtectedPathsMatchers() {
        List<AntPathRequestMatcher> protectedPaths = new ArrayList<>();
        protectedPaths.add(new AntPathRequestMatcher(PATH_REFRESH_TOKEN));
        
        protectedPaths.add(new AntPathRequestMatcher(PATH_UPDATE_COLLEGE, HttpMethod.PUT.name()));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_REGISTRAR));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_DEAN));
        
        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_DEAN_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_COLLEGE_REGISTRAR_PROFILE));
        
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_USER_SMSNOTIFICATIONENABLED));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_USER_EMAILNOTIFICATIONENABLED));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_USER_NOTIFICATION_ENABLED));
        
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_SMC_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_NMC_PROFILE));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_NBE_PROFILE));

        protectedPaths.add(new AntPathRequestMatcher(PATH_DASHBOARD_ROOT + PATH_DASHBOARD_FETCH_DETAILS));
        protectedPaths.add(new AntPathRequestMatcher(PATH_DASHBOARD_ROOT + PATH_DASHBOARD_CARD_COUNT));

        protectedPaths.add(new AntPathRequestMatcher(PATH_HEALTH_PROFESSIONAL_ROOT + PATH_HEALTH_PROFESSIONAL_APPLICATIONS));

        return protectedPaths.toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN, HttpMethod.POST.name());
    }
}
