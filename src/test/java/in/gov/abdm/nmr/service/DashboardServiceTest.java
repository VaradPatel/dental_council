package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.IFetchCountOnCardRepository;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsCustomRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.service.impl.DashboardServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @InjectMocks
    DashboardServiceImpl dashboardService;

    @Mock
    IAccessControlService accessControlService;
    @Mock
    ISmcProfileRepository iSmcProfileRepository;
    @Mock
    IFetchCountOnCardRepository iFetchCountOnCardRepository;
    @Mock
    ICollegeProfileDaoService iCollegeProfileDaoService;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    IFetchSpecificDetailsCustomRepository iFetchSpecificDetailsCustomRepository;
    @Mock
    ICollegeProfileDaoService collegeProfileDaoService;

    @Test
    void testFetchCountOnCardShouldFetchCountOnCardForStateMedicalCouncil() throws AccessDeniedException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        when(iSmcProfileRepository.getSmcIdByUserId(any(BigInteger.class))).thenReturn(List.of(BigInteger.ONE));
        when(iFetchCountOnCardRepository.fetchCountForSmc(any(BigInteger.class))).thenReturn(List.of());
        FetchCountOnCardResponseTO fetchCountOnCardResponseTO = dashboardService.fetchCountOnCard();
        assertEquals("1,7", fetchCountOnCardResponseTO.getHpRegistrationRequest().getApplicationTypeIds());
    }

    @Test
    void testFetchCountOnCardShouldFetchCountOnCardForStateMedicalCollege() throws AccessDeniedException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iCollegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        FetchCountOnCardResponseTO fetchCountOnCardResponseTO = dashboardService.fetchCountOnCard();
        assertEquals("1,7", fetchCountOnCardResponseTO.getHpRegistrationRequest().getApplicationTypeIds());
    }

    @Test
    void testFetchCountOnCardShouldFetchCountOnCardForNationalMedicalCouncil() throws AccessDeniedException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        FetchCountOnCardResponseTO fetchCountOnCardResponseTO = dashboardService.fetchCountOnCard();
        assertEquals("1,7", fetchCountOnCardResponseTO.getHpRegistrationRequest().getApplicationTypeIds());
    }

    @Test
    void testFetchCountOnCardShouldFetchCountOnCardForNBE() throws AccessDeniedException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.NBE.getId()));
        FetchCountOnCardResponseTO fetchCountOnCardResponseTO = dashboardService.fetchCountOnCard();
        assertEquals("1,7", fetchCountOnCardResponseTO.getHpRegistrationRequest().getApplicationTypeIds());
    }

    @Test
    void testFetchCountOnCardShouldThrowAccessDeniedException() {
        when(accessControlService.getLoggedInUser()).thenReturn(null);
        assertThrows(AccessDeniedException.class, () -> dashboardService.fetchCountOnCard());
    }


    public static DashboardResponseTO getDashboardResponse() {
        DashboardResponseTO responseTO = new DashboardResponseTO();
        responseTO.setTotalNoOfRecords(BigInteger.ONE);
        return responseTO;
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForStateMedicalCouncilAndSearchWithApplicantFullName() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        when(iSmcProfileRepository.findByUserId(any(BigInteger.class))).thenReturn(getSmcProfile());
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS, APPLICANT_FULL_NAME_IN_LOWER_CASE, PROFILE_DISPLAY_NAME, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForCollegeAndSearchWithStateMedicalCouncil() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS, COUNCIL_NAME_IN_LOWER_CASE, STATE_MEDICAL_COUNCIL, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForPermanentSuspensionRequestsReceivedAndSearchWithRegistrationNumber() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS, REGISTRATION_NUMBER_IN_LOWER_CASE, REGISTRATION_NUMBER, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForPermanentSuspensionRequestsApprovedAndSearchWithGender() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS, GENDER_IN_LOWER_CASE, GENDER, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForTotalSuspensionRequestsSearchWithEmailId() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS, EMAIL_ID_IN_LOWER_CASE, CommonTestData.EMAIL_ID, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForTotalUserGroupsAndSearchWithMobileNumber() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("", "", TOTAL, MOBILE_NUMBER_IN_LOWER_CASE, MOBILE_NUMBER, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }

    @Test
    void testFetchCardDetailsShouldFetchCardDetailsForSearchWithYearOfRegistration() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(iFetchSpecificDetailsCustomRepository.fetchDashboardData(any(DashboardRequestParamsTO.class), any(Pageable.class))).thenReturn(getDashboardResponse());
        when(collegeProfileDaoService.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        DashboardResponseTO dashboardResponseTO = dashboardService.fetchCardDetails("1", "3,4", NMRConstants.TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS, YEAR_OF_REGISTRATION_IN_LOWER_CASE, REGISTRATION_YEAR, 1, 1, "", "");
        assertEquals(BigInteger.ONE, dashboardResponseTO.getTotalNoOfRecords());
    }
}
