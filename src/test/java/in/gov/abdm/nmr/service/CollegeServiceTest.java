package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.CollegeMaster;
import in.gov.abdm.nmr.entity.Password;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.repository.IPasswordRepository;
import in.gov.abdm.nmr.service.impl.CollegeServiceImpl;
import in.gov.abdm.nmr.service.impl.PasswordDaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.getCollegeMaster;
import static in.gov.abdm.nmr.util.CommonTestData.getPassword;
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

}