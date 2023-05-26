package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.security.jwt.JwtUtil;
import in.gov.abdm.nmr.service.impl.AuthServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthServiceImpl authService;
    @Mock
    JwtUtil jwtUtil;

    @Mock
    IUserDaoService userDetailDaoService;
    @Mock
    IHpProfileDaoService hpProfileService;
    @Mock
    ICollegeProfileDaoService collegeProfileDaoService;
    @Mock
    ISmcProfileDaoService smcProfileDaoService;
    @Mock
    GatewayFClient gatewayFClient;

    HttpServletResponse response;

    @BeforeEach
    void setup() {
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForHealthProfessional() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(hpProfileService.findLatestEntryByUserid(any(BigInteger.class))).thenReturn(getHpProfile());
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForCollege() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForSMC() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        when(smcProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getSmcProfile());
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

    @Test
    void testSessionsShouldThrowException() {
        assertThrows(Exception.class, () -> authService.sessions(SessionRequestTo.builder().clientId(CLIENT_ID).clientSecret(CLIENT_SECRET).build()));
    }
}