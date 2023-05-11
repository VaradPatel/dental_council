package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.mapper.CourseMasterToMapper;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.service.impl.*;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MasterDataServiceImplTest {

    @InjectMocks
    private MasterDataServiceImpl masterDataService;
    @Mock
    StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;
    @Mock
    BroadSpecialityServiceImpl broadSpecialityServiceImpl;
    @Mock
    CountryServiceImpl countryService;
    @Mock
    IMasterDataMapper masterDataMapper;
    @Mock
    StateServiceImpl stateService;
    @Mock
    DistrictServiceImpl districtService;
    @Mock
    SubDistrictServiceImpl subDistrictService;
    @Mock
    VillagesServiceImpl villagesServiceImpl;
    @Mock
    LanguageServiceImpl languageService;
    @Mock
    ICourseService courseService;
    @Mock
    IRegistrationRenewationTypeService registrationRenewationTypeService;
    @Mock
    IFacilityTypeService facilityTypeService;
    @Mock
    ICollegeMasterService collegeMasterService;
    @Mock
    IUniversityMasterService universityMasterService;

    @Test
    void testSmcs() {
        when(stateMedicalCouncilService.getAllStateMedicalCouncil())
                .thenReturn(CommonTestData.getStateMedicalCouncilTo());
        List<MasterDataTO> result = masterDataService.smcs();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertNotNull(STATE_MEDICAL_COUNCIL, result.get(0).getName());
    }

    @Test
    void testSpecialities() {
        when(broadSpecialityServiceImpl.getSpecialityData()).thenReturn(getBroadSpecialityTo());
        when(masterDataMapper.specialitiesToMasterDataTOs(getBroadSpecialityTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.specialities();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCountries() {
        when(countryService.getCountryData()).thenReturn(getCountryTo());
        when(masterDataMapper.countriesToMasterDataTOs(getCountryTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.countries();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testStates() {
        when(stateService.getStateData(ID)).thenReturn(getStateTo());
        when(masterDataMapper.statesToMasterDataTOs(getStateTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.states(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testDistricts() {
        when(districtService.getDistrictData(ID)).thenReturn(getDistrictTo());
        when(masterDataMapper.districtsToMasterDataTOs(getDistrictTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.districts(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testSubDistricts() {
        when(subDistrictService.getSubDistrictData(ID)).thenReturn(getSubDistrictTo());
        when(masterDataMapper.subDistrictsToMasterDataTOs(getSubDistrictTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.subDistricts(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCities() {
        when(villagesServiceImpl.getCityData(ID)).thenReturn(getVillageTo());
        when(masterDataMapper.citiesToMasterDataTOs(getVillageTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.cities(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testLanguages() {
        when(languageService.getLanguageData()).thenReturn(getLanguangesTo());
        when(masterDataMapper.languagesToMasterDataTOs(getLanguangesTo())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.languages();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCourses() {
        when(courseService.getCourseData()).thenReturn(getCourseTo());
        List<MasterDataTO> result = masterDataService.courses();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testRegistrationRenewationType() {
        when(registrationRenewationTypeService.getRegistrationRenewationType()).thenReturn(getRegistrationRenewationTypeTO());
        when(masterDataMapper.registrationRenewationTypeDataTOs(getRegistrationRenewationTypeTO())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.registrationRenewationType();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testFacility() {
        when(facilityTypeService.getFacilityType()).thenReturn(getFacilityTypeTO());
        when(masterDataMapper.facilityTypeDataTOs(getFacilityTypeTO())).thenReturn(getListOfMasterDataTO());
        List<MasterDataTO> result = masterDataService.facilityType();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testGetCollegesByState() {
        when(collegeMasterService.getCollegesByStateId(ID)).thenReturn(getListOfCollegeMasterTo());
        when(masterDataMapper.collegeMasterResponseDataTo(getListOfCollegeMasterTo())).thenReturn(getListOfCollegeMasterResponseTo());
        List<CollegeMasterResponseTo> result = masterDataService.getCollegesByState(ID);
        assertEquals(ID, result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testGetUniversitiesByCollege() {
        when(universityMasterService.getUniversitiesByCollegeId(ID)).thenReturn(getListOfUniversityMasterTo());
        when(masterDataMapper.universityMasterResponseDataTo(getListOfUniversityMasterTo())).thenReturn(getListOfUniversityMasterResponseTo());
        List<UniversityMasterResponseTo> result = masterDataService.getUniversitiesByCollege(ID);
        assertEquals(ID, result.get(0).getId());
        assertEquals(1, result.size());
    }

}