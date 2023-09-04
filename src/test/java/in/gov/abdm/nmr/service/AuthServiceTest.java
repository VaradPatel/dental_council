package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.enums.LoginTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import in.gov.abdm.nmr.security.jwt.JwtUtil;
import in.gov.abdm.nmr.service.impl.AuthServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import in.gov.abdm.nmr.util.TestUsernamePasswordAuthenticationToken;
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
import static org.mockito.ArgumentMatchers.*;
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
    IWorkFlowRepository workFlowRepository;
    @Mock
    IHpProfileRepository ihpProfileRepository;
    @Mock
    GatewayFClient gatewayFClient;

    HttpServletResponse response;


    @BeforeEach
    void setup() {
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForHealthProfessional() {

        TestUsernamePasswordAuthenticationToken  testUsernamePasswordAuthenticationToken =  new TestUsernamePasswordAuthenticationToken(null, null, UserTypeEnum.HEALTH_PROFESSIONAL.getId(), LoginTypeEnum.USERNAME_PASSWORD.getCode(), null);
        SecurityContextHolder.getContext().setAuthentication(testUsernamePasswordAuthenticationToken);
        when(userDetailDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(hpProfileService.findLatestEntryByUserid(any(BigInteger.class))).thenReturn(getHpProfile());
        when(ihpProfileRepository.findLatestHpProfileFromWorkFlow(nullable(String.class))).thenReturn(getHpProfile());
        when(workFlowRepository.findLastWorkFlowForHealthProfessional(any(BigInteger.class))).thenReturn(null);
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForCollege() {
        TestUsernamePasswordAuthenticationToken  testUsernamePasswordAuthenticationToken =  new TestUsernamePasswordAuthenticationToken(null, null, UserTypeEnum.COLLEGE.getId(), LoginTypeEnum.USERNAME_PASSWORD.getCode(), null);
        SecurityContextHolder.getContext().setAuthentication(testUsernamePasswordAuthenticationToken);
        when(userDetailDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

    @Test
    void testSuccessfulAuthSuccessfulAuthForSMC() {
        TestUsernamePasswordAuthenticationToken  testUsernamePasswordAuthenticationToken =  new TestUsernamePasswordAuthenticationToken(null, null, UserTypeEnum.SMC.getId(), LoginTypeEnum.USERNAME_PASSWORD.getCode(), null);
        SecurityContextHolder.getContext().setAuthentication(testUsernamePasswordAuthenticationToken);
        when(userDetailDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        when(smcProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getSmcProfile());
        LoginResponseTO loginResponseTO = authService.successfulAuth(response);
        assertEquals(ID, loginResponseTO.getUserId());
    }

  }