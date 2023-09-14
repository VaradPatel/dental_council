package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileStatus;
import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.entity.UserAttachments;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidFileUploadException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.ApplicationServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    IHpProfileRepository iHpProfileRepository;

    @Mock
    IRequestCounterService requestCounterService;

    @Mock
    IUserDaoService userDaoService;

    @Mock
    IWorkFlowService iWorkFlowService;
    @Mock
    IWorkFlowRepository iWorkFlowRepository;
    @Mock
    IWorkFlowCustomRepository iWorkFlowCustomRepository;
    @Mock
    IFetchTrackApplicationDetailsCustomRepository iFetchTrackApplicationDetailsCustomRepository;
    @Mock
    IRegistrationDetailRepository iRegistrationDetailRepository;
    @Mock
    IWorkFlowAuditRepository iWorkFlowAuditRepository;

    @Mock
    UserAttachmentsRepository userAttachmentsRepository;

    NMRPagination nmrPagination = NMRPagination.builder().pageNo(1).offset(1).sortBy("id").sortType("ASC").build();

    @Test
    void testSuspendRequestShouldThrowNmrExceptionWhenHpProfileIsNotApproved() {
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(iHpProfileRepository.findLatestHpProfileFromWorkFlow(any(String.class))).thenReturn(getHpProfile());
        assertThrows(WorkFlowException.class, () -> applicationService.suspendRequest(getApplicationRequestTo()));
    }

    @Test
    void testSuspendRequestShouldCreateSuspensionRequestForApprovedProfile() throws WorkFlowException, NmrException, InvalidRequestException {
        HpProfile hpProfile = getHpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.APPROVED.getId()).build());
        RequestCounter requestCounter = RequestCounter.builder().counter(BigInteger.valueOf(1))
                .applicationType(in.gov.abdm.nmr.entity.ApplicationType.builder().id(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()).requestPrefixId("NMR300").build()).build();

        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(hpProfile);
        when(iHpProfileRepository.findLatestHpProfileFromWorkFlow(any(String.class))).thenReturn(hpProfile);
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(requestCounter);
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        applicationService.suspendRequest(getApplicationRequestTo());
        Mockito.verify(iWorkFlowService, Mockito.times(1)).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
    }

    private ApplicationRequestTo getApplicationRequestTo() {
        ApplicationRequestTo applicationRequestTo = new ApplicationRequestTo();
        applicationRequestTo.setApplicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
        applicationRequestTo.setHpProfileId(CommonTestData.ID);
        applicationRequestTo.setRemarks("Suspend");
        applicationRequestTo.setFromDate(Timestamp.valueOf("2023-12-01 00:00:00"));
        applicationRequestTo.setToDate(Timestamp.valueOf("2024-12-01 00:00:00"));
        return applicationRequestTo;
    }

    private MultipartFile getMultipartFile(){
        return new MockMultipartFile("functionTestingFile", "filename.pdf", "text/plain",
                "some xml".getBytes());
    }

    @Test
    void testReactivateRequest() throws WorkFlowException, InvalidRequestException, IOException, InvalidFileUploadException {
        RequestCounter requestCounter = RequestCounter.builder().counter(BigInteger.valueOf(1))
                .applicationType(in.gov.abdm.nmr.entity.ApplicationType.builder().id(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()).requestPrefixId("NMR300").build()).build();
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfileAsSuspendedStatus());
        when(iHpProfileRepository.findLatestHpProfileFromWorkFlow(any(String.class))).thenReturn(getHpProfileAsSuspendedStatus());
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(requestCounter);
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userAttachmentsRepository.save(any(UserAttachments.class))).thenReturn(getUserAttachments());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        ReactivateRequestResponseTo reactivateRequestResponseTo = applicationService.reactivateRequest(getMultipartFile(),getApplicationRequestTo());
        assertEquals("1", reactivateRequestResponseTo.getProfileId());
        assertEquals(NMRConstants.SUCCESS_RESPONSE, reactivateRequestResponseTo.getMessage());
    }

    @Test
    void testReactivateRequestShouldPreviousGroupIsNmc() throws WorkFlowException, InvalidRequestException, IOException, InvalidFileUploadException {
        RequestCounter requestCounter = RequestCounter.builder().counter(BigInteger.valueOf(1))
                .applicationType(in.gov.abdm.nmr.entity.ApplicationType.builder().id(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()).requestPrefixId("NMR300").build()).build();
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfileAsSuspendedStatus());
        when(iHpProfileRepository.findLatestHpProfileFromWorkFlow(any(String.class))).thenReturn(getHpProfileAsSuspendedStatus());
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(requestCounter);
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        when(userAttachmentsRepository.save(any(UserAttachments.class))).thenReturn(getUserAttachments());
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        ReactivateRequestResponseTo reactivateRequestResponseTo = applicationService.reactivateRequest(getMultipartFile(),getApplicationRequestTo());
        assertEquals("1", reactivateRequestResponseTo.getProfileId());
        assertEquals(NMRConstants.SUCCESS_RESPONSE, reactivateRequestResponseTo.getMessage());
    }

    private static UserAttachments getUserAttachments() {
        UserAttachments userAttachments =  new UserAttachments();
        userAttachments.setRequestId(CommonTestData.REQUEST_ID);
        userAttachments.setId(CommonTestData.ID);
        userAttachments.setAttachmentTypeId(AttachmentType.REACTIVATION.getId());
        return userAttachments;
    }


    public static ReactivateHealthProfessionalResponseTO getReactivateHealthProfessionalResponse() {
        ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO = new ReactivateHealthProfessionalResponseTO();
        reactivateHealthProfessionalResponseTO.setTotalNoOfRecords(BigInteger.ONE);
        return reactivateHealthProfessionalResponseTO;
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByApplicantFullName() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", APPLICANT_FULL_NAME_IN_LOWER_CASE, FIRST_NAME, "", "");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByRegistrationNumber() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", REGISTRATION_NUMBER_IN_LOWER_CASE, REGISTRATION_NUMBER, "", "");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByEmailId() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", EMAIL_ID_IN_LOWER_CASE, CommonTestData.EMAIL_ID, "", "");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByMobileNumber() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", MOBILE_NUMBER_IN_LOWER_CASE, MOBILE_NUMBER, "", "");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByYearOfRegistration() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", YEAR_OF_REGISTRATION_IN_LOWER_CASE, REGISTRATION_YEAR, "ID", "DSC");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearch() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", SEARCH_IN_LOWER_CASE, REGISTRATION_YEAR, "ID", "DSC");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }


    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldThrowInvalidSearchCriteria() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));

        assertThrows(InvalidRequestException.class, () -> applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", GENDER, GENDER, "ID", "DSC"));
    }

    @Test
    void testGetReactivationRecordsOfHealthProfessionalsToNmcShouldSearchByGender() throws InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(any(ReactivateHealthProfessionalRequestParam.class), any(Pageable.class)))
                .thenReturn(getReactivateHealthProfessionalResponse());
        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(
                "1", "1", GENDER_IN_LOWER_CASE, GENDER, "", "");
        assertEquals(BigInteger.ONE, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }

    @Test
    void testFetchApplicationDetails() throws InvalidRequestException {
        when(iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(
                any(HealthProfessionalApplicationRequestParamsTo.class), any(Pageable.class), any(List.class)))
                .thenReturn(getHealthProfessionalApplicationResponse());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo =
                applicationService.fetchApplicationDetails(nmrPagination, WORK_FLOW_STATUS_ID_IN_LOWER_CASE, "1", "", "");
        assertEquals(BigInteger.ONE, healthProfessionalApplicationResponseTo.getTotalNoOfRecords());
        assertEquals(PROFILE_DISPLAY_NAME, healthProfessionalApplicationResponseTo.getHealthProfessionalApplications().get(0).getApplicantFullName());
    }

    @Test
    void testFetchApplicationDetailsForHealthProfessional() throws InvalidRequestException {
        when(iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(getRegistrationDetail());
        when(iRegistrationDetailRepository.fetchHpProfileIdByRegistrationNumber(anyString())).thenReturn(List.of(BigInteger.ONE));
        when(iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(
                any(HealthProfessionalApplicationRequestParamsTo.class), any(Pageable.class), any(List.class)))
                .thenReturn(getHealthProfessionalApplicationResponse());
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo = applicationService.
                fetchApplicationDetailsForHealthProfessional(
                        BigInteger.ONE, "1", "1", "", "", WORK_FLOW_STATUS_ID_IN_LOWER_CASE, "1");
        assertEquals(BigInteger.ONE, healthProfessionalApplicationResponseTo.getTotalNoOfRecords());
        assertEquals(PROFILE_DISPLAY_NAME, healthProfessionalApplicationResponseTo.getHealthProfessionalApplications().get(0).getApplicantFullName());
    }

    @Test
    void testFetchApplicationDetail() throws InvalidRequestException {
        ApplicationDetailResponseTo applicationDetailResponseTo = getApplicationDetailResponseTo();
        when(iFetchTrackApplicationDetailsCustomRepository.fetchApplicationDetails(anyString())).thenReturn(applicationDetailResponseTo);
        ApplicationDetailResponseTo response = applicationService.fetchApplicationDetail(CommonTestData.REQUEST_ID);
        assertEquals(CommonTestData.REQUEST_ID, response.getRequestId());
    }

    private ApplicationDetailResponseTo getApplicationDetailResponseTo() {
        ApplicationDetailResponseTo  applicationDetailResponseTo =  new ApplicationDetailResponseTo();
        applicationDetailResponseTo.setRequestId(CommonTestData.REQUEST_ID);
        applicationDetailResponseTo.setApplicationType(ApplicationType.HP_REGISTRATION.getId());
        applicationDetailResponseTo.setPendency(1l);
        applicationDetailResponseTo.setCurrentStatus(BigInteger.ONE);
        applicationDetailResponseTo.setDegreeName(DEGREE);
        applicationDetailResponseTo.setCurrentGroupId(Group.SMC.getId());
        applicationDetailResponseTo.setSubmissionDate(SUBMISSION_DATE.toString());
        ApplicationDetailsTo applicationDetailsTo = new ApplicationDetailsTo();
        applicationDetailsTo.setRemarks("remarks");
        applicationDetailsTo.setWorkflowStatusId(WorkflowStatus.PENDING.getId());
        applicationDetailsTo.setActionId(Action.APPROVED.getId());
        applicationDetailsTo.setActionDate(SUBMISSION_DATE.toString());
        applicationDetailResponseTo.setApplicationDetails(List.of(applicationDetailsTo));
        return applicationDetailResponseTo;
    }

    @Test
    void testFetchApplicationDetailShouldThrowInvalidRequestException() throws InvalidRequestException {
        when(iFetchTrackApplicationDetailsCustomRepository.fetchApplicationDetails(anyString())).thenThrow(InvalidRequestException.class);
        assertThrows(InvalidRequestException.class, () -> applicationService.fetchApplicationDetail(CommonTestData.REQUEST_ID));
    }

}
