package in.gov.abdm.nmr.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.entity.Course;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.service.ICourseService;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import in.gov.abdm.nmr.service.IRegistrationRenewationTypeService;

@ExtendWith(MockitoExtension.class)
class MasterDataServiceImplTest {

    @InjectMocks
    private MasterDataServiceImpl masterDataService;
    @Mock
    StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;
    @Mock
    CountryServiceImpl countryService;
    @Mock
    StateServiceImpl stateService;
    @Mock
    DistrictServiceImpl districtService;
    @Mock
    VillagesServiceImpl villagesServiceImpl;
    @Mock
    BroadSpecialityServiceImpl broadSpecialityServiceImpl;
    @Mock
    UniversityServiceImpl universityService;
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
    List<BroadSpecialityTO> broadSpecialityList;
    List<MasterDataTO> expected;
    List<StateMedicalCouncilTO> smcList;
    List<CountryTO> countryList;
    List<StateTO> stateList;
    List<DistrictTO> districtList;
    List<SubDistrictTO> subDistrictList;
    BigInteger id;
    List<UniversityTO> universityList;
    List<CollegeTO> collegeList;
    List<LanguageTO> languageList;
    List<CourseTO> courseList;
    List<Course> courses;
    List<RegistrationRenewationTypeTO> registrationRenewationTypeList;
    List<FacilityTypeTO> facilityTypeList;
    List<VillagesTO> villagesList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        broadSpecialityList = new ArrayList<>();
        expected = Collections.singletonList(new MasterDataTO());
        smcList = new ArrayList<>();
        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
        id = BigInteger.ONE;
        districtList = new ArrayList<>();
        universityList = new ArrayList<>();
        collegeList = new ArrayList<>();
        languageList = new ArrayList<>();
        courseList = new ArrayList<>();
        registrationRenewationTypeList = new ArrayList<>();
        facilityTypeList = new ArrayList<>();
        villagesList = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        broadSpecialityList = null;
        expected = null;
        smcList = null;
        countryList = null;
        stateList = null;
        id = null;
        districtList = null;
        universityList = null;
        collegeList = null;
        languageList = null;
        courseList = null;
        registrationRenewationTypeList = null;
        facilityTypeList = null;
        villagesList = null;
    }

    @Test
    void testSmcs() {
        when(stateMedicalCouncilService.smcs()).thenReturn(smcList);
        when(masterDataMapper.stateMedicalCouncilsToMasterDataTOs(smcList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.smcs();
        assertEquals(expected, result);
    }

    @Test
    void testSmcsNullTest() {
        when(stateMedicalCouncilService.smcs()).thenReturn(null);
        List<MasterDataTO> res = masterDataService.smcs();
        assertTrue(res.isEmpty());
    }

    @Test
    void testSpecialities() {
        when(broadSpecialityServiceImpl.getSpecialityData()).thenReturn(broadSpecialityList);
        when(masterDataMapper.specialitiesToMasterDataTOs(broadSpecialityList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.specialities();
        assertEquals(expected, result);
    }

    @Test
    void testSpecialitiesNullTest() {
        when(broadSpecialityServiceImpl.getSpecialityData()).thenReturn(null);
        List<MasterDataTO> res = masterDataService.specialities();
        assertTrue(res.isEmpty());
    }

    @Test
    void testCountries() {
        when(countryService.getCountryData()).thenReturn(countryList);
        when(masterDataMapper.countriesToMasterDataTOs(countryList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.countries();
        assertEquals(expected, result);
    }

    @Test
    void testCountriesNullResult() {
        when(countryService.getCountryData()).thenReturn(null);
        List<MasterDataTO> res = masterDataService.countries();
        assertTrue(res.isEmpty());
    }

    @Test
    void testStates() {
        when(stateService.getStateData(id)).thenReturn(stateList);
        when(masterDataMapper.statesToMasterDataTOs(stateList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.states(id);
        assertEquals(expected, result);
    }

    @Test
    void testStatesNullResult() {
        when(stateService.getStateData(id)).thenReturn(null);
        List<MasterDataTO> res = masterDataService.states(id);
        assertTrue(res.isEmpty());
    }

    @Test
    void testDistricts() {
        when(districtService.getDistrictData(id)).thenReturn(districtList);
        when(masterDataMapper.districtsToMasterDataTOs(districtList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.districts(id);
        assertEquals(expected, result);
    }

    @Test
    void testDistrictsNullResult() {
        when(districtService.getDistrictData(id)).thenReturn(null);
        List<MasterDataTO> res = masterDataService.districts(id);
        assertTrue(res.isEmpty());
    }

    @Test
    void testCities() {
        when(villagesServiceImpl.getCityData(BigInteger.ONE)).thenReturn(villagesList);
        when(masterDataMapper.citiesToMasterDataTOs(villagesList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.cities(BigInteger.ONE);
        assertEquals(expected, result);
    }

    @Test
    void testCitiesNullResult() {
        when(villagesServiceImpl.getCityData(id)).thenReturn(null);
        List<MasterDataTO> res = masterDataService.cities(BigInteger.ONE);
        assertTrue(res.isEmpty());
    }

    @Test
    void testUniversities() {
        when(universityService.getUniversityData()).thenReturn(universityList);
        when(masterDataMapper.universitiesToMasterDataTOs(universityList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.universities();
        assertEquals(expected, result);
    }

    @Test
    void testUniversitiesNullResult() {
        when(universityService.getUniversityData()).thenReturn(null);
        List<MasterDataTO> result = masterDataService.universities();
        assertTrue(result.isEmpty());
    }

    @Test
    void testLanguages() {
        when(languageService.getLanguageData()).thenReturn(languageList);
        when(masterDataMapper.languagesToMasterDataTOs(languageList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.languages();
        assertEquals(expected, result);
    }

    @Test
    void testLanguagesNullResult() {
        when(languageService.getLanguageData()).thenReturn(null);
        List<MasterDataTO> result = masterDataService.languages();
        assertTrue(result.isEmpty());
    }

    @Test
    void testRegistrationRenewationType() {
        when(registrationRenewationTypeService.getRegistrationRenewationType()).thenReturn(registrationRenewationTypeList);
        when(masterDataMapper.registrationRenewationTypeDataTOs(registrationRenewationTypeList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.registrationRenewationType();
        assertEquals(expected, result);
    }

    @Test
    void testRegistrationRenewationTypeNullResult() {
        when(registrationRenewationTypeService.getRegistrationRenewationType()).thenReturn(null);
        List<MasterDataTO> result = masterDataService.registrationRenewationType();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFacilityType() {
        when(facilityTypeService.getFacilityType()).thenReturn(facilityTypeList);
        when(masterDataMapper.facilityTypeDataTOs(facilityTypeList)).thenReturn(expected);
        List<MasterDataTO> result = masterDataService.facilityType();
        assertEquals(expected, result);
    }

    @Test
    void testFacilityTypeNullResult() {
        when(facilityTypeService.getFacilityType()).thenReturn(null);
        List<MasterDataTO> res = masterDataService.facilityType();
        assertTrue(res.isEmpty());
    }

    @Test
    void testFacilityTypeIncorrectMapping() {
        when(facilityTypeService.getFacilityType()).thenReturn(facilityTypeList);
        when(masterDataMapper.facilityTypeDataTOs(facilityTypeList)).thenReturn(new ArrayList<>());
        List<MasterDataTO> result = masterDataService.facilityType();
        assertNotEquals(expected, result);
    }
}