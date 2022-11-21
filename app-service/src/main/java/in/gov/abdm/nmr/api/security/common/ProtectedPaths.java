package in.gov.abdm.nmr.api.security.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ProtectedPaths {

    private ProtectedPaths() {
    }

    public static final String PATH_LOGIN = "/login";
    public static final String PATH_REFRESH_TOKEN = "/refreshToken";
    public static final String PATH_TEST = "/test";

    public static AntPathRequestMatcher[] getProtectedPathsMatchers() {
        List<String> protectedPaths = new ArrayList<>();
        protectedPaths.add(PATH_REFRESH_TOKEN);
        protectedPaths.add(PATH_TEST);

        return protectedPaths.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(PATH_LOGIN);
    }
}
