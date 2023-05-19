package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeResponseTo;
import in.gov.abdm.nmr.entity.CollegeMaster;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.NotFoundException;
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
import java.util.Arrays;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollegeServiceTest {

    @InjectMocks
    CollegeServiceImpl collegeService;
    @Mock
    private ICollegeMasterDaoService collegeMasterDaoService;
    @Mock
    private IUniversityMasterService universityMasterService;
    @Mock
    private IStateMedicalCouncilDaoService stateMedicalCouncilDaoService;
    @Mock
    private ICollegeProfileDaoService collegeProfileDaoService;
    @Mock
    private IUserDaoService userDaoService;
    @Mock
    private EntityManager entityManager;

    @Mock
    private IPasswordService passwordService;


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
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(collegeMasterDaoService.findById(any(BigInteger.class))).thenReturn(getCollegeMaster());
        when(collegeProfileDaoService.findAdminByCollegeId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(universityMasterService.getUniversitiesByCollegeId(ID)).thenReturn(getListOfUniversityMasterTo());
        //when(stateMedicalCouncilDaoService.findByState(anyString())).thenReturn(getStateMedicalCouncil());
        CollegeResponseTo collegeResponseTo = collegeService.getCollege(ID);
        assertEquals(EMAIL_ID, collegeResponseTo.getEmailId());
    }
}