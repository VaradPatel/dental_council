package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.WorkNature;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.HpProfileDaoServiceImpl;
import in.gov.abdm.nmr.util.AuditLogPublisher;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HpProfileDaoServiceTest {
    @InjectMocks
    HpProfileDaoServiceImpl hpProfileDaoService;

    @Mock
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Mock
    private ICouncilService councilService;
    @Mock
    private IHpProfileRepository iHpProfileRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private IAddressRepository iAddressRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private IStateRepository stateRepository;
    @Mock
    private SubDistrictRepository subDistrictRepository;
    @Mock
    private VillagesRepository villagesRepository;
    @Mock
    IRegistrationDetailRepository iRegistrationDetailRepository;
    private MockMultipartFile certificate;
    @Mock
    IRegistrationDetailRepository registrationDetailRepository;
    @Mock
    HpNbeDetailsRepository hpNbeDetailsRepository;
    @Mock
    IStateMedicalCouncilRepository iStateMedicalCouncilRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    ICollegeMasterRepository collegeMasterRepository;
    @Mock
    UniversityMasterRepository universityMasterRepository;
    @Mock
    IQualificationDetailRepository qualificationDetailRepository;
    @Mock
    IForeignQualificationDetailRepository iCustomQualificationDetailRepository;
    @Mock
    WorkProfileRepository workProfileRepository;
    @Mock
    WorkNatureRepository workNatureRepository;
    @Mock
    WorkStatusRepository workStatusRepository;
    @Mock
    LanguagesKnownRepository languagesKnownRepository;
    @Mock
    AuditLogPublisher auditLogPublisher;

    @BeforeEach
    void setup() {
        certificate = new MockMultipartFile("certificate", "certificate.pdf",
                ".pdf", "{\"Test\": \"Certificate\"}".getBytes());
    }

    @Test
    void testFindById() {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfile()));
        HpProfile hpProfile = hpProfileDaoService.findById(ID);
        assertEquals(ID, hpProfile.getId());
        assertEquals(EMAIL_ID, hpProfile.getEmailId());
    }

    @Test
    void testFindLatestEntryByUserid() {
        when(iHpProfileRepository.findLatestEntryByUserid(ID)).thenReturn(getHpProfile());
        HpProfile hpProfile = hpProfileDaoService.findLatestEntryByUserid(ID);
        assertEquals(ID, hpProfile.getId());
        assertEquals(EMAIL_ID, hpProfile.getEmailId());
    }

    @Test
    void testFetchSmcRegistrationDetailShouldReturnMatchingHealthProfessionalDetails() throws NmrException, NoDataFoundException {
        when(stateMedicalCouncilRepository.findStateMedicalCouncilById(any(BigInteger.class))).thenReturn(CommonTestData.getStateMedicalCouncil());
        when(councilService.getCouncilByRegistrationNumberAndCouncilName(anyString(), anyString())).thenReturn(List.of(CommonTestData.getImrCouncilDetails()));

        HpSmcDetailTO hpSmcDetailTO = hpProfileDaoService.fetchSmcRegistrationDetail(1, REGISTRATION_NUMBER);
        assertEquals(PROFILE_DISPLAY_NAME, hpSmcDetailTO.getHpName());
        assertEquals(REGISTRATION_NUMBER, hpSmcDetailTO.getRegistrationNumber());
        assertEquals(STATE_MEDICAL_COUNCIL, hpSmcDetailTO.getCouncilName());
    }

    @Test
    void testUpdateHpPersonalDetailsWithExistingProfile() throws InvalidRequestException, WorkFlowException {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfileInApprovedStatus()));
        //when(iHpProfileRepository.findLatestHpProfileByRegistrationId(REGISTRATION_NUMBER)).thenReturn(getHpProfileInApprovedStatus());
        when(countryRepository.findById(ID)).thenReturn(Optional.of(getCountry()));
        when(iHpProfileRepository.save(ArgumentMatchers.any())).thenReturn(getHpProfileInApprovedStatus());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 4)).thenReturn(getCommunicationAddress());
        Address newAddress = getCommunicationAddress();
        when(countryRepository.findById(newAddress.getCountry().getId())).thenReturn(Optional.of(getCountry()));
        when(stateRepository.findById(newAddress.getState().getId())).thenReturn(Optional.of(getState()));
        when(districtRepository.findById(newAddress.getDistrict().getId())).thenReturn(Optional.of(getDistrict()));
        when(subDistrictRepository.findById(newAddress.getSubDistrict().getId())).thenReturn(Optional.of(getSubDistrict()));
        when(villagesRepository.findById(newAddress.getVillage().getId())).thenReturn(Optional.of(getVillage()));
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(newAddress);
        //when(iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(ID)).thenReturn(new RegistrationDetails());
        //when(iRegistrationDetailRepository.save(ArgumentMatchers.any())).thenReturn(new RegistrationDetails());
        //when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 5)).thenReturn(getKYCAddress());
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(getKYCAddress());
        doNothing().when(auditLogPublisher).publishHpProfileAuditLog(any(HpProfile.class));
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(ID, getHpPersonalUpdateRequestTo());
        assertEquals(ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUpdateHpPersonalDetails() throws InvalidRequestException, WorkFlowException {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfile()));
        when(countryRepository.findById(ID)).thenReturn(Optional.of(getCountry()));
        when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 4)).thenReturn(getCommunicationAddress());
        Address newAddress = getCommunicationAddress();
        when(countryRepository.findById(newAddress.getCountry().getId())).thenReturn(Optional.of(getCountry()));
        when(stateRepository.findById(newAddress.getState().getId())).thenReturn(Optional.of(getState()));
        when(districtRepository.findById(newAddress.getDistrict().getId())).thenReturn(Optional.of(getDistrict()));
        when(subDistrictRepository.findById(newAddress.getSubDistrict().getId())).thenReturn(Optional.of(getSubDistrict()));
        when(villagesRepository.findById(newAddress.getVillage().getId())).thenReturn(Optional.of(getVillage()));
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(newAddress);
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(ID, getHpPersonalUpdateRequestTo());
        assertEquals(ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUpdateHpRegistrationDetails() throws NmrException, InvalidRequestException {
        when(registrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(getRegistrationDetail());
        when(iHpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(iStateMedicalCouncilRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getStateMedicalCouncil()));
        when(countryRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getCountry()));
        when(stateRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getState()));
        when(collegeMasterRepository.findCollegeMasterById(any(BigInteger.class))).thenReturn(getCollegeMaster());
        when(universityMasterRepository.findUniversityMasterById(any(BigInteger.class))).thenReturn(getUniversityMaster());
        when(courseRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getCourse()));
        when(qualificationDetailRepository.saveAll(any(List.class))).thenReturn(new ArrayList());
        when(iCustomQualificationDetailRepository.saveAll(any(List.class))).thenReturn(new ArrayList());
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(HP_ID, getHpRegistrationUpdateRequestTo(), certificate, List.of(certificate), certificate, certificate);
        assertEquals(HP_ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUpdateHpRegistrationDetailsShouldUpdateHpRegistrationDetailsForInternationalQualification() throws NmrException, InvalidRequestException {
        when(registrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(getRegistrationDetail());
        when(iHpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(hpNbeDetailsRepository.findByUserId(any(BigInteger.class))).thenReturn(getHPNbeDetails());
        when(iStateMedicalCouncilRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getStateMedicalCouncil()));
        when(qualificationDetailRepository.saveAll(any(List.class))).thenReturn(new ArrayList());
        when(iCustomQualificationDetailRepository.saveAll(any(List.class))).thenReturn(new ArrayList());
        //when(iCustomQualificationDetailRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getForeignQualificationDetails()));
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(HP_ID, getHpRegistrationUpdateRequestForInternationalQualification(), certificate, List.of(certificate), certificate, certificate);
        assertEquals(HP_ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUpdateWorkProfileDetails() throws NotFoundException, InvalidRequestException, NmrException {
        when(iHpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        //when(workProfileRepository.getWorkProfileDetailsByUserId(any(BigInteger.class))).thenReturn(List.of(getWorkProfile()));
        when(workNatureRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(new WorkNature()));
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateWorkProfileDetails(HP_ID, getHpWorkProfileUpdateRequest(), List.of(certificate, certificate));
        assertEquals(HP_ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUploadHpProfilePhoto() throws InvalidRequestException, IOException, InvalidFileUploadException {
        when(iHpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(iHpProfileRepository.save(any(HpProfile.class))).thenReturn(getHpProfile());
        HpProfilePictureResponseTO hpProfilePictureResponseTO = hpProfileDaoService.uploadHpProfilePhoto(certificate, HP_ID);
        assertEquals(SUCCESS_RESPONSE, hpProfilePictureResponseTO.getMessage());
    }
}
