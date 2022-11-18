package in.gov.abdm.nmr.api.security.common;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import in.gov.abdm.nmr.api.security.controller.AuthController;

public class ProtectedPaths {

    private ProtectedPaths() {
    }

    private static final List<String> protectedPathList = Arrays.asList(AuthController.PATH_REFRESH_TOKEN, AuthController.PATH_TEST);

    public static List<String> getProtectedPaths() {
        return protectedPathList;
    }

    public static AntPathRequestMatcher[] getProtectedPathsMatchers() {
        return protectedPathList.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new);
    }

    public static AntPathRequestMatcher getLoginPathMatcher() {
        return new AntPathRequestMatcher(AuthController.PATH_LOGIN);
    }
}
