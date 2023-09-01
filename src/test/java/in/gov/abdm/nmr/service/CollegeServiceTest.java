package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.CollegeMaster;
import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.service.impl.CollegeServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollegeServiceTest {

    @InjectMocks
    CollegeServiceImpl collegeService;
    @Mock
    ICollegeMasterDaoService collegeMasterDaoService;
    @Mock
    IUniversityMasterService universityMasterService;
    @Mock
    IStateMedicalCouncilDaoService stateMedicalCouncilDaoService;
    @Mock
    ICollegeProfileDaoService collegeProfileDaoService;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    EntityManager entityManager;
    @Mock
    IPasswordService passwordService;


    @BeforeEach
    void setup() {
    }

    @Test
    void testGetAllColleges() throws NmrException {
        when(collegeMasterDaoService.getAllColleges()).thenReturn(Arrays.asList(getCollegeMaster()));
        collegeService.getAllColleges();
        verify(collegeMasterDaoService, times(1)).save((CollegeMaster) collegeMasterDaoService.getAllColleges());
    }


    @Test
    void testGetCollege() throws NotFoundException, NmrException, InvalidIdException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeMasterDaoService.findById(any(BigInteger.class))).thenReturn(getCollegeMaster());
        when(collegeProfileDaoService.findAdminByCollegeId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(universityMasterService.getUniversitiesByCollegeId(ID)).thenReturn(getListOfUniversityMasterTo());
        CollegeResponseTo collegeResponseTo = collegeService.getCollege(ID);
        assertEquals(EMAIL_ID, collegeResponseTo.getEmailId());
    }


    public static CollegeResponseTo getCollegeResponse() {
        CollegeResponseTo collegeResponseTo = new CollegeResponseTo();
        collegeResponseTo.setId(ID);
        collegeResponseTo.setUniversityTO(getUniversityTo());
        return collegeResponseTo;
    }


    public static User getNmcUser() {
        User user = new User();
        user.setUserSubType(getUserSubType(UserSubTypeEnum.NMC_ADMIN.getId()));
        return user;
    }

    @Test
    void testCreateOrUpdateCollege() throws ResourceExistsException, NotFoundException, NmrException, InvalidRequestException, InvalidIdException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getNmcUser());
        when(collegeMasterDaoService.findById(any(BigInteger.class))).thenReturn(getCollegeMaster());
        when(collegeProfileDaoService.findAdminByCollegeId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        when(collegeMasterDaoService.save(any(CollegeMaster.class))).thenReturn(getCollegeMaster());
        when(universityMasterService.findById(any(BigInteger.class))).thenReturn(getUniversityMaster());
        when(userDaoService.save(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(passwordService.getResetPasswordLink(any(SendLinkOnMailTo.class),anyString())).thenReturn(ResponseMessageTo.builder().build());
        CollegeResponseTo collegeResponse = collegeService.createOrUpdateCollege(getCollegeResponse());
        assertEquals(ID, collegeResponse.getId());
    }

    @Test
    void testCreateOrUpdateCollegeShouldThrowInvalidRequestException() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        assertThrows(InvalidRequestException.class, () -> collegeService.createOrUpdateCollege(getCollegeResponse()));
    }

    @Test
    void testCreateOrUpdateCollegeShouldThrowNotFoundExceptionForCollege() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getNmcUser());
        when(collegeMasterDaoService.findById(any(BigInteger.class))).thenReturn(null);
        assertThrows(NotFoundException.class, () -> collegeService.createOrUpdateCollege(getCollegeResponse()));
    }

    public static CollegeResponseTo getCollege() {
        CollegeResponseTo collegeResponseTo = new CollegeResponseTo();
        collegeResponseTo.setUniversityTO(getUniversityTo());
        collegeResponseTo.setEmailId(EMAIL_ID);
        collegeResponseTo.setMobileNumber(MOBILE_NUMBER);
        collegeResponseTo.setStateTO(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        collegeResponseTo.setDistrictTO(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        collegeResponseTo.setStateMedicalCouncilTO(new StateMedicalCouncilTO(SMC_ID, SMC_CODE, SMC_NAME));

        return collegeResponseTo;
    }

    @Test
    void testCreateOrUpdateCollegeShouldThrowExceptionWhenNMCRegisterCollegeWithExistingContact() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getNmcUser());
        when(userDaoService.existsByEmailAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        assertThrows(Exception.class, () -> collegeService.createOrUpdateCollege(getCollege()));
    }

    public static CollegeProfileTo getCollegeProfileRequest() {
        CollegeProfileTo collegeProfileTo = new CollegeProfileTo();
        collegeProfileTo.setId(ID);
        collegeProfileTo.setCollegeId(COLLEGE_ID);
        collegeProfileTo.setEmailId(EMAIL_ID);
        collegeProfileTo.setName(PROFILE_DISPLAY_NAME);
        collegeProfileTo.setMobileNumber(MOBILE_NUMBER);
        collegeProfileTo.setDesignation(DESIGNATION);
        return collegeProfileTo;
    }

    @Test
    void testCreateOrUpdateCollegeVerifier() throws NmrException, ResourceExistsException, GeneralSecurityException, InvalidRequestException, InvalidIdException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeProfileDaoService.findById(any(BigInteger.class))).thenReturn(getCollegeProfile());
        when(userDaoService.save(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(collegeProfileDaoService.save(any(CollegeProfile.class))).thenReturn(getCollegeProfile());
        CollegeProfileTo collegeVerifier = collegeService.createOrUpdateCollegeVerifier(getCollegeProfileRequest());
        assertEquals(ID, collegeVerifier.getId());
    }


    @Test
    void testGetCollegeVerifier() throws NmrException, InvalidIdException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeProfileDaoService.findById(any(BigInteger.class))).thenReturn(getCollegeProfile());
        CollegeProfileTo collegeVerifier = collegeService.getCollegeVerifier(COLLEGE_ID, ID);
        assertEquals(COLLEGE_ID, collegeVerifier.getCollegeId());
    }

    @Test
    void testGetAllCollegeVerifiersDesignation() {
        assertThrows(Exception.class, () -> collegeService.getAllCollegeVerifiersDesignation());
    }
}