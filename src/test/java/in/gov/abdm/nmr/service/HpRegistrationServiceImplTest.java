package in.gov.abdm.nmr.service;


import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.ESignStatus;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.mapper.HpProfileRegistrationMapper;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.impl.HpRegistrationServiceImpl;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.FAILURE_RESPONSE;
import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HpRegistrationServiceImplTest {

    @InjectMocks
    HpRegistrationServiceImpl hpRegistrationService;

    @Mock
    IHpProfileDaoService hpProfileDaoService;
    @Mock
    IAddressRepository iAddressRepository;
    @Mock
    IWorkFlowRepository workFlowRepository;
    private MockMultipartFile certificate;
    @Mock
    IRegistrationDetailRepository registrationDetailRepository;
    @Mock
    HpNbeDetailsRepository hpNbeDetailsRepository;
    @Mock
    IQualificationDetailRepository qualificationDetailRepository;
    @Mock
    HpProfileRegistrationMapper hpProfileRegistrationMapper;
    @Mock
    IForeignQualificationDetailRepository customQualificationDetailRepository;
    @Mock
    IHpProfileMapper iHpProfileMapper;
    @Mock
    IRequestCounterService requestCounterService;
    @Mock
    IWorkFlowService iWorkFlowService;
    @Mock
    IHpProfileRepository iHpProfileRepository;
    @Mock
    ResetTokenRepository resetTokenRepository;
    @Mock
    LanguagesKnownRepository languagesKnownRepository;
    @Mock
    WorkProfileRepository workProfileRepository;
    @Mock
    IQueriesService iQueriesService;
    @Mock
    IStateMedicalCouncilRepository stateMedicalCouncilRepository;
    @Mock
    ICouncilService councilService;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    IPasswordDaoService passwordDaoService;
    @Mock
    CountryRepository countryRepository;
    @Mock
    IHpProfileStatusRepository hpProfileStatusRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    RsaUtil rsaUtil;
    @Mock
    EntityManager entityManager;
    @Mock
    IRegistrationDetailRepository iRegistrationDetailRepository;
    @Mock
    IStateRepository stateRepository;
    @Mock
    DistrictRepository districtRepository;
    @Mock
    OtpServiceImpl otpService;
    @Mock
    IHpProfileMasterRepository iHpProfileMasterRepository;
    @Mock
    INotificationService notificationService;
    @Mock
    IUserDaoService userDetailDaoService;
    @Mock
    IQualificationDetailRepository iQualificationDetailRepository;

    @Mock
    IAccessControlService accessControlService;

    @Mock
    ITrackApplicationReadStatusRepository iTrackApplicationReadStatusRepository;

    @Mock
    IOtpValidationService otpValidationService;

    @BeforeEach
    void setup() {
        certificate = new MockMultipartFile("certificate", "certificate.pdf",
                "application/json", "{\"name\": \"Emp Name\"}".getBytes());
    }


    @Test
    void testFuzzyScore() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Govind Kedia", "Govind Dwarkadasji Kedia");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreLogic() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Sathish Chonde", "Sathish Udhavroa Chonde");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreImplementation() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Vijay Anthony Chandrasaekar", "Vijay Chandrasaekar");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testAddOrUpdateHpPersonalDetail() throws WorkFlowException, InvalidRequestException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iHpProfileRepository.findLatestEntryByUserid(CommonTestData.ID)).thenReturn(getHpProfile());
        when(hpProfileDaoService.updateHpPersonalDetails(any(BigInteger.class), any(HpPersonalUpdateRequestTO.class)))
                .thenReturn(new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", HP_ID));
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(any(BigInteger.class), any(Integer.class))).thenReturn(getCommunicationAddress());
        when(workFlowRepository.findLastWorkFlowForHealthProfessional(any(BigInteger.class))).thenReturn(getWorkFlow());
        when(iTrackApplicationReadStatusRepository.findByUserId(any(BigInteger.class))).thenReturn(null);
        HpProfilePersonalResponseTO responseTO = hpRegistrationService.addOrUpdateHpPersonalDetail(CommonTestData.ID, new HpPersonalUpdateRequestTO());

        assertEquals(CommonTestData.ID, responseTO.getHpProfileId());
    }

    public static RegistrationDetails getRegistrationDetails() {
        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setHpProfileId(getHpProfile());
        return registrationDetails;
    }

    public static QualificationDetails getQualificationDetails() {
        QualificationDetails qualificationDetails = new QualificationDetails();
        return qualificationDetails;
    }

    @Test
    void testAddOrUpdateHpRegistrationDetail() throws InvalidRequestException, NmrException {
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iHpProfileRepository.findLatestEntryByUserid(CommonTestData.ID)).thenReturn(getHpProfile());
        when(hpProfileDaoService.updateHpRegistrationDetails(any(BigInteger.class), any(HpRegistrationUpdateRequestTO.class),
                any(MultipartFile.class), (any(List.class)), certificate, certificate))
                .thenReturn(new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", HP_ID));
        when(registrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class)))
                .thenReturn(getRegistrationDetails());
        when(hpNbeDetailsRepository.findByUserId(any(BigInteger.class))).thenReturn(getHPNbeDetails());
        when(qualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class)))
                .thenReturn(List.of(getQualificationDetails()));
        when(customQualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class)))
                .thenReturn(List.of(getForeignQualificationDetails()));
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO1 = new HpProfileRegistrationResponseTO();
        hpProfileRegistrationResponseTO1.setQualificationDetailResponseTos(Collections.emptyList());
        when(hpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(
                any(RegistrationDetails.class), any(HpNbeDetails.class),
                anyList(), anyList()))
                .thenReturn(hpProfileRegistrationResponseTO1);
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpRegistrationService.addOrUpdateHpRegistrationDetail(CommonTestData.ID, new HpRegistrationUpdateRequestTO(),
                certificate, List.of(certificate), certificate, certificate);
        assertEquals(HP_ID, hpProfileRegistrationResponseTO.getHpProfileId());
    }

    @Test
    void testFetchSmcRegistrationDetailShouldGetStateMedicalCouncilDetails() throws NoDataFoundException {
        when(hpProfileDaoService.fetchSmcRegistrationDetail(any(Integer.class), anyString())).thenReturn(getHpSmcDetail());
        when(iHpProfileMapper.smcRegistrationToDto(any(HpSmcDetailTO.class))).thenReturn(getSmcRegistrationDetailResponse());
        SmcRegistrationDetailResponseTO detailResponseTO = hpRegistrationService.fetchSmcRegistrationDetail(SMC_ID.intValue(), REGISTRATION_NUMBER);
        assertEquals(REGISTRATION_NUMBER, detailResponseTO.getRegistrationNumber());
        assertEquals(HP_NAME, detailResponseTO.getHpName());
        assertEquals(REGISTRATION_NUMBER, detailResponseTO.getRegistrationNumber());
        assertEquals(STATE_MEDICAL_COUNCIL, detailResponseTO.getCouncilName());
        assertEquals(HP_ID, detailResponseTO.getHpProfileId());
        assertEquals(CommonTestData.EMAIL_ID, detailResponseTO.getEmailId());
    }

    @Test
    void testUploadHpProfilePicture() throws InvalidRequestException, IOException, InvalidFileUploadException {
        when(hpProfileDaoService.uploadHpProfilePhoto(any(MultipartFile.class), any(BigInteger.class))).
                thenReturn(getHpProfilePictureResponse());
        when(iHpProfileMapper.hpProfilePictureUploadToDto(any(HpProfilePictureResponseTO.class))).thenReturn(getHpProfilePictureResponse());
        HpProfilePictureResponseTO hpProfilePictureResponseTO = hpRegistrationService.uploadHpProfilePicture(certificate, CommonTestData.ID);
        assertEquals(1, hpProfilePictureResponseTO.getStatus());
    }

    @Test
    void testAddQualificationShouldAddQualificationDetailsAndReturnSuccess() throws WorkFlowException, NmrException, InvalidRequestException, IOException, InvalidFileUploadException {
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfileForNMR());
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(getRequestCounter());
        when(iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(any(BigInteger.class),anyList())).thenReturn(true);
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        String s = hpRegistrationService.addQualification(CommonTestData.ID, getQualification(), List.of(certificate), certificate);
        assertEquals(SUCCESS_RESPONSE, s);
    }

    @Test
    void testAddQualificationShouldAddQualificationDetailsAndReturnSuccess2() throws WorkFlowException, NmrException, InvalidRequestException, IOException, InvalidFileUploadException {
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfileForNMR());
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(getRequestCounter());
        when(iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(any(BigInteger.class),anyList())).thenReturn(true);
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        String s = hpRegistrationService.addQualification(CommonTestData.ID, getQualification(), List.of(certificate), certificate);
        assertEquals(SUCCESS_RESPONSE, s);
    }

    @Test
    void testAddQualificationShouldThrowErrorForSuspendedProfile() throws WorkFlowException, NmrException, InvalidRequestException {
        HpProfile hpProfile=getHpProfileForNMR();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.SUSPENDED.getId()).build());
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(hpProfile);
        assertThrows(WorkFlowException.class, () -> hpRegistrationService.addQualification(CommonTestData.ID, getQualification(), List.of(certificate), certificate));
    }

    @Test
    void testAddQualificationShouldThrowExceptionQualificationWorkFlowCreationFail() throws WorkFlowException, NmrException, InvalidRequestException {
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfile());
        assertThrows(WorkFlowException.class, () -> hpRegistrationService.addQualification(CommonTestData.ID, getQualification(), List.of(certificate), certificate));
    }

    @Test
    void testAddOrUpdateWorkProfileDetail() throws NotFoundException, NmrException, InvalidRequestException {
        when(hpProfileDaoService.updateWorkProfileDetails(CommonTestData.ID, getHpWorkProfileUpdateRequest(), List.of(certificate)))
                .thenReturn(new HpProfileUpdateResponseTO(1, SUCCESS_RESPONSE, HP_ID));
        when(iHpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(iHpProfileRepository.findLatestEntryByUserid(CommonTestData.ID)).thenReturn(getHpProfile());
        when(workProfileRepository.getWorkProfileDetailsByUserId(any(BigInteger.class))).thenReturn(List.of(getWorkProfile()));
        when(languagesKnownRepository.findByUserId(any(BigInteger.class))).thenReturn(List.of(new LanguagesKnown()));
        when(accessControlService.getLoggedInUser()).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        hpRegistrationService.addOrUpdateWorkProfileDetail(CommonTestData.ID, getHpWorkProfileUpdateRequest(), List.of(certificate));
    }

    public static HpSubmitRequestTO getHpSubmitRequest() {
        HpSubmitRequestTO hpSubmitRequestTO = new HpSubmitRequestTO();
        hpSubmitRequestTO.setRequestId(CommonTestData.REQUEST_ID);
        hpSubmitRequestTO.setTransactionId(TRANSACTION_ID);
        hpSubmitRequestTO.setApplicationTypeId(ApplicationType.FOREIGN_HP_REGISTRATION.getId());
        hpSubmitRequestTO.setESignStatus(ESignStatus.PROFILE_ESIGNED_WITH_SAME_AADHAR.getId());
        hpSubmitRequestTO.setHpProfileId(HP_ID);
        hpSubmitRequestTO.setHprShareAcknowledgement(1);
        return hpSubmitRequestTO;
    }

    @Test
    void testSubmitHpProfileShouldSubmitHealthProfessionalProfile() throws WorkFlowException, InvalidRequestException {
        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(any(BigInteger.class))).thenReturn(false);
        when(workFlowRepository.findLastWorkFlowForHealthProfessional(any(BigInteger.class))).thenReturn(getWorkFlow());
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(getRequestCounter());
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        when(registrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(getRegistrationDetails());
        when(customQualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class))).thenReturn(List.of(getForeignQualificationDetails()));
        HpProfileAddResponseTO hpProfileAddResponseTO = hpRegistrationService.submitHpProfile(getHpSubmitRequest());
        assertEquals(HP_ID, hpProfileAddResponseTO.getHpProfileId());
    }


    public WorkFlow getWorkFlowForQueryRaised() {
        WorkFlow workFlow = new WorkFlow();
        workFlow.setId(CommonTestData.ID);
        workFlow.setRequestId(CommonTestData.REQUEST_ID);
        workFlow.setWorkFlowStatus(new WorkFlowStatus(WorkflowStatus.QUERY_RAISED.getId(), WorkflowStatus.QUERY_RAISED.getDescription()));
        return workFlow;
    }

    @Test
    void testSubmitHpProfileShouldSubmitHealthProfessionalProfileForQueryRaised() throws WorkFlowException, InvalidRequestException {
        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(any(BigInteger.class))).thenReturn(false);
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(workFlowRepository.findLastWorkFlowForHealthProfessional(any(BigInteger.class))).thenReturn(getWorkFlowForQueryRaised());
        when(iQueriesService.markQueryAsClosed(any(String.class))).thenReturn(getResponseMessage());
        HpProfileAddResponseTO hpProfileAddResponseTO = hpRegistrationService.submitHpProfile(getHpSubmitRequest());
        assertEquals(HP_ID, hpProfileAddResponseTO.getHpProfileId());
    }

    @Test
    void testSubmitHpProfileShouldThrowWorkFlowException() {
        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(any(BigInteger.class))).thenReturn(true);
        assertThrows(WorkFlowException.class, () -> hpRegistrationService.submitHpProfile(getHpSubmitRequest()));
    }


    @Test
    void testUserKycFuzzyMatchShouldFailFuzzyMatch() throws ParseException {
        KycResponseMessageTo kycResponseMessageTo = hpRegistrationService.userKycFuzzyMatch(List.of(getImrCouncilDetails()), REGISTRATION_NUMBER, SMC_ID, getUserKyc().getName(), getUserKyc().getGender(), getUserKyc().getBirthDate());
        assertEquals(FAILURE_RESPONSE, kycResponseMessageTo.getKycFuzzyMatchStatus());
    }

    public static NewHealthPersonalRequestTO getHealthPersonalRequest() {
        NewHealthPersonalRequestTO healthPersonalRequestTO = new NewHealthPersonalRequestTO();
        healthPersonalRequestTO.setGender(GENDER);
        healthPersonalRequestTO.setSmcId(SMC_ID.toString());
        healthPersonalRequestTO.setRegistrationNumber(REGISTRATION_NUMBER);
        healthPersonalRequestTO.setBirthdate(DATE_OF_BIRTH.toString());
        healthPersonalRequestTO.setState(STATE_NAME);
        healthPersonalRequestTO.setDistrict(DISTRICT_NAME);
        healthPersonalRequestTO.setUsername(TEST_USER);
        healthPersonalRequestTO.setMobileNumber(MOBILE_NUMBER);
        healthPersonalRequestTO.setEmail(CommonTestData.EMAIL_ID);
        healthPersonalRequestTO.setName(FULL_NAME);

        return healthPersonalRequestTO;
    }

    @Test
    void testAddNewHealthProfessional() throws GeneralSecurityException, DateException, InvalidRequestException, NmrException, ParseException {
        when(userDaoService.save(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(stateMedicalCouncilRepository.findStateMedicalCouncilById(any(BigInteger.class))).thenReturn(CommonTestData.getStateMedicalCouncil());
        when(councilService.getCouncilByRegistrationNumberAndCouncilName(anyString(), anyString())).thenReturn(List.of());
        when(countryRepository.findByName(anyString())).thenReturn(getCountry());
        when(hpProfileStatusRepository.findById(any(BigInteger.class))).thenReturn(Optional.ofNullable(getHPProfileStatus()));
        when(iRegistrationDetailRepository.save(ArgumentMatchers.any())).thenReturn(new RegistrationDetails());
        when(iHpProfileRepository.save(any(HpProfile.class))).thenReturn(getHpProfile());
        when(stateRepository.findByName(anyString())).thenReturn(getState());
        when(districtRepository.findByDistrictNameAndStateId(anyString(), any(BigInteger.class))).thenReturn(getDistrict());
        ResponseMessageTo responseMessageTo = hpRegistrationService.addNewHealthProfessional(getHealthPersonalRequest());
        assertEquals(SUCCESS_RESPONSE, responseMessageTo.getMessage());
    }

    @Test
    void testAddNewHealthProfessionalShouldThrowUserNameAlreadyRegistered() {
        when(userDaoService.existsByUserNameAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        assertThrows(InvalidRequestException.class, () -> hpRegistrationService.addNewHealthProfessional(getHealthPersonalRequest()));
    }

    @Test
    void testAddNewHealthProfessionalShouldThrowMobileNumberAlreadyRegistered() {
        when(userDaoService.existsByMobileNumberAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        assertThrows(InvalidRequestException.class, () -> hpRegistrationService.addNewHealthProfessional(getHealthPersonalRequest()));
    }

    @Test
    void testAddNewHealthProfessionalShouldThrowEmailIdAlreadyRegistered() {
        when(userDaoService.existsByEmailAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        assertThrows(InvalidRequestException.class, () -> hpRegistrationService.addNewHealthProfessional(getHealthPersonalRequest()));
    }

    @Test
    void testUpdateHealthProfessionalEmailMobile() throws OtpException, InvalidRequestException {
        when(otpValidationService.isOtpVerified(anyString())).thenReturn(false);
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(iHpProfileRepository.save(any(HpProfile.class))).thenReturn(getHpProfile());
        when(iHpProfileRepository.findMasterHpProfileByHpProfileId(any(BigInteger.class))).thenReturn(CommonTestData.ID);
        doNothing().when(iHpProfileMasterRepository).updateMasterHpProfileMobile(any(BigInteger.class), anyString());
        when(iHpProfileMasterRepository.findHpProfileMasterById(any(BigInteger.class))).thenReturn(getMasterHpProfile());
        HealthProfessionalPersonalRequestTo healthProfessionalPersonalRequest = getHealthProfessionalPersonalRequest();
        healthProfessionalPersonalRequest.setMobileNumber("9999911111");
        hpRegistrationService.updateHealthProfessionalEmailMobile(CommonTestData.ID, healthProfessionalPersonalRequest);
        Mockito.verify(iHpProfileRepository,times(2)).save(any());
    }

    @Test
    void testGetEmailVerificationLink() {
        when(iHpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(userDaoService.checkEmailUsedByOtherUser(any(BigInteger.class), anyString(), any(BigInteger.class))).thenReturn(false);
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(userDaoService.save(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iHpProfileRepository.save(any(HpProfile.class))).thenReturn(getHpProfile());
        when(resetTokenRepository.findByUserNameAndUserType(anyString(),any(BigInteger.class))).thenReturn(getResetToken());
        hpRegistrationService.getEmailVerificationLink(CommonTestData.ID, new VerifyEmailLinkTo(CommonTestData.EMAIL_ID));
    }

    @Test
    void testDeLinkCurrentWorkDetailsShouldThrowUserNotFoundException() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(null);
        assertThrows(NmrException.class, () -> hpRegistrationService.delinkCurrentWorkDetails(
                new WorkDetailsDelinkRequest(List.of(CommonTestData.FACILITY_ID))));
    }

}
