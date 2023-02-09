package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.service.ICollegeDaoService;
import in.gov.abdm.nmr.service.ICourseService;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import in.gov.abdm.nmr.service.IRegistrationRenewationTypeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class MasterDataServiceImplTest {

    @InjectMocks
    MasterDataServiceImpl masterDataServiceImpl;

    @Mock
    StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;

    @Mock
    CountryServiceImpl countryService;

    @Mock
    StateServiceImpl stateService;

    @Mock
    DistrictServiceImpl districtService;

    @Mock
    SubDistrictServiceImpl subDistrictService;

    @Mock
    VillagesServiceImpl villagesServiceImpl;

    @Mock
    BroadSpecialityServiceImpl broadSpecialityServiceImpl;

    @Mock
    UniversityServiceImpl universityService;

    @Mock
    ICollegeDaoService collegeService;

    @Mock
    LanguageServiceImpl languageService;

    @Mock
    IMasterDataMapper masterDataMapper;

    @Mock
    ICourseService courseService;

    @Mock
    IRegistrationRenewationTypeService registrationRenewationTypeService;

    @Mock
    IFacilityTypeService facilityTypeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void smcs() {
    }

    @Test
    void specialities() {
    }

    @Test
    void countries() {
    }

    @Test
    void states() {
    }

    @Test
    void districts() {
    }

    @Test
    void subDistricts() {
    }

    @Test
    void cities() {
    }

    @Test
    void universities() {
    }

    @Test
    void colleges() {
    }

    @Test
    void languages() {
    }

    @Test
    void courses() {
    }

    @Test
    void registrationRenewationType() {
    }

    @Test
    void facilityType() {
    }
}