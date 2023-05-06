package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.entity.FacilityType;
import in.gov.abdm.nmr.mapper.FacilityTypeDtoMapper;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.repository.FacilityTypeRepository;
import in.gov.abdm.nmr.repository.ICollegeProfileRepository;
import in.gov.abdm.nmr.service.impl.CollegeProfileDaoServiceImpl;
import in.gov.abdm.nmr.service.impl.FacilityTypeServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.FACILITY_NAME;
import static in.gov.abdm.nmr.util.CommonTestData.ID;
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
}