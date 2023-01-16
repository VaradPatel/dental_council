package in.gov.abdm.nmr.security.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ProtectedPaths {

    private ProtectedPaths() {
    }

    public static final String PATH_LOGIN = "/login";
    public static final String PATH_REFRESH_TOKEN = "/refreshToken";
    public static final String PATH_UPDATE_COLLEGE = "/college";
    public static final String PATH_REGISTER_COLLEGE_REGISTRAR = "/college/registrar";
    public static final String PATH_REGISTER_COLLEGE_DEAN = "/college/dean";
    public static final String PATH_USER_ROOT = "/user";
    public static final String PATH_USER_SMSNOTIFICATIONENABLED = "/smsNotificationEnabled";
    public static final String PATH_USER_EMAILNOTIFICATIONENABLED = "/emailNotificationEnabled";

    public static AntPathRequestMatcher[] getProtectedPathsMatchers() {
        List<AntPathRequestMatcher> protectedPaths = new ArrayList<>();
        protectedPaths.add(new AntPathRequestMatcher(PATH_REFRESH_TOKEN));
        protectedPaths.add(new AntPathRequestMatcher(PATH_UPDATE_COLLEGE, HttpMethod.PUT.name()));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_REGISTRAR));
        protectedPaths.add(new AntPathRequestMatcher(PATH_REGISTER_COLLEGE_DEAN));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_USER_SMSNOTIFICATIONENABLED));
        protectedPaths.add(new AntPathRequestMatcher(PATH_USER_ROOT + PATH_USER_EMAILNOTIFICATIONENABLED));

        return protectedPaths.toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN, HttpMethod.POST.name());
    }
}
