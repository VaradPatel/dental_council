package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.impl.*;
import in.gov.abdm.nmr.util.CommonTestData;
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
                .thenReturn(CommonTestData.getStateMedicalCouncilTos());
        List<MasterDataTO> result = masterDataService.smcs();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertNotNull(STATE_MEDICAL_COUNCIL, result.get(0).getName());
    }

    @Test
    void testSpecialities() {
        when(broadSpecialityServiceImpl.getSpecialityData()).thenReturn(getBroadSpecialityTo());
        List<MasterDataTO> result = masterDataService.specialities();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCountries() {
        when(countryService.getCountryData()).thenReturn(getCountryTo());
        List<MasterDataTO> result = masterDataService.countries();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testStates() {
        when(stateService.getStateData(ID)).thenReturn(getStates());
        List<MasterDataTO> result = masterDataService.states(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testDistricts() {
        when(districtService.getDistrictData(ID)).thenReturn(getDistricts());
        List<MasterDataTO> result = masterDataService.districts(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testSubDistricts() {
        when(subDistrictService.getSubDistrictData(ID)).thenReturn(getSubDistricts());
        List<MasterDataTO> result = masterDataService.subDistricts(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCities() {
        when(villagesServiceImpl.getCityData(ID)).thenReturn(getVillages());
        List<MasterDataTO> result = masterDataService.cities(ID);
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testLanguages() {
        when(languageService.getLanguageData()).thenReturn(getLanguangesTo());
        List<MasterDataTO> result = masterDataService.languages();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testCourses() {
        when(courseService.getCourseData()).thenReturn(getCourses());
        List<MasterDataTO> result = masterDataService.courses();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testRegistrationRenewationType() {
        when(registrationRenewationTypeService.getRegistrationRenewationType()).thenReturn(getRegistrationRenewationTypeTO());
        List<MasterDataTO> result = masterDataService.registrationRenewationType();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testFacility() {
        when(facilityTypeService.getFacilityType()).thenReturn(getFacilityTypeTO());
        List<MasterDataTO> result = masterDataService.facilityType();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testGetCollegesByState() {
        when(collegeMasterService.getCollegesByStateId(ID)).thenReturn(getListOfCollegeMasterTo());
        List<CollegeMasterResponseTo> result = masterDataService.getCollegesByState(ID);
        assertEquals(ID, result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void testGetUniversitiesByCollege() {
        when(universityMasterService.getUniversitiesByCollegeId(ID)).thenReturn(getListOfUniversityMasterTo());
        List<UniversityMasterResponseTo> result = masterDataService.getUniversitiesByCollege(ID);
        assertEquals(ID, result.get(0).getId());
        assertEquals(1, result.size());
    }

}