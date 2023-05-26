package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.repository.ICollegeProfileRepository;
import in.gov.abdm.nmr.service.impl.CollegeProfileDaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.COLLEGE_ID;
import static in.gov.abdm.nmr.util.CommonTestData.getCollegeProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollegeProfileDaoServiceTest {

    @InjectMocks
    CollegeProfileDaoServiceImpl collegeProfileDaoService;
    @Mock
    private ICollegeProfileRepository collegeProfileRepository;

    CollegeProfile collegeProfile;

    @BeforeEach
    void setup() {
        collegeProfile = new CollegeProfile();
    }

    @Test
    void testSave() {
        when(collegeProfileRepository.save(collegeProfile)).thenReturn(collegeProfile);
        collegeProfileDaoService.save(collegeProfile);
        verify(collegeProfileRepository, times(1)).save(collegeProfileDaoService.save(any(CollegeProfile.class)));
    }

    @Test
    void testFindAdminByCollegeId() {
        when(collegeProfileRepository.findAdminByCollegeId(any(BigInteger.class), any(BigInteger.class))).thenReturn(getCollegeProfile());
        CollegeProfile clgProfile = collegeProfileDaoService.findAdminByCollegeId(COLLEGE_ID);
        assertEquals(COLLEGE_ID, clgProfile.getId());
    }

    @Test
    void testFindById() {
        when(collegeProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getCollegeProfile()));
        CollegeProfile clgProfile = collegeProfileDaoService.findById(COLLEGE_ID);
        assertEquals(COLLEGE_ID, clgProfile.getId());
    }

    @Test
    void testFindByUserId() {
        when(collegeProfileRepository.findByUserId(any(BigInteger.class))).thenReturn(getCollegeProfile());
        CollegeProfile clgProfile = collegeProfileDaoService.findByUserId(COLLEGE_ID);
        assertEquals(COLLEGE_ID, clgProfile.getId());
    }
}