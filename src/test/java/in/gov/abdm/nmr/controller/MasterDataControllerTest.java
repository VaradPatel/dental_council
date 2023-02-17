package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.IMasterDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MasterDataControllerTest {

    @InjectMocks
    MasterDataController masterDataController;

    @Mock
    IMasterDataService masterDataService;

    private MockMvc mockMvc;

    private List<MasterDataTO> expectedResult;

    List<MasterDataTO> expected;

    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        mockMvc = MockMvcBuilders.standaloneSetup(masterDataController).build();
        expectedResult = new ArrayList<>();
        expectedResult.add(new MasterDataTO(1L, "HOSPITAL", "Hospital"));
        expectedResult.add(new MasterDataTO(2L, "HOSPITAL", "Hospital"));
    }

    @AfterEach
    void tearDown() {
        expected = null;
        mockMvc = null;
        expectedResult = null;
    }

    @Test
    public void testSmcs() {
        when(masterDataService.smcs()).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.smcs();
        assertEquals(expected, actual);
    }

    @Test
    public void testSpecialities() {
        when(masterDataService.specialities()).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.specialities();
        assertEquals(expected, actual);
    }

    @Test
    public void testCountries() {
        when(masterDataService.countries()).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.countries();
        assertEquals(expected, actual);
    }

    @Test
    public void testStates() {
        BigInteger countryId = BigInteger.ONE;
        when(masterDataService.states(countryId)).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.states(countryId);
        assertEquals(expected, actual);
    }

    @Test
    public void testDistricts() {
        BigInteger stateId = BigInteger.ONE;
        when(masterDataService.districts(stateId)).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.districts(stateId);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubDistricts() {
        BigInteger districtId = BigInteger.ONE;
        when(masterDataService.subDistricts(districtId)).thenReturn(expected);
        List<MasterDataTO> actual = masterDataController.subDistricts(districtId);
        assertEquals(expected, actual);
    }

    @Test
    void testCities() {
        BigInteger subDistrictId = BigInteger.valueOf(1);
        when(masterDataService.cities(subDistrictId)).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.cities(subDistrictId);
        assertEquals(expectedResult, result);
    }

    @Test
    void testUniversities() {
        when(masterDataService.universities()).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.universities();
        assertEquals(expectedResult, result);
    }


    @Test
    void testColleges() {
        BigInteger universityId = BigInteger.valueOf(1);
        when(masterDataService.colleges(universityId)).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.colleges(universityId);
        assertEquals(expectedResult, result);
    }

    @Test
    void testLanguages() throws Exception {
        when(masterDataService.languages()).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.languages();
        assertEquals(expectedResult, result);
    }

    @Test
    void testCourses() throws Exception {
        when(masterDataService.courses()).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.courses();
        assertEquals(expectedResult, result);
    }

    @Test
    void testRegistrationRenewationType() {
        when(masterDataService.registrationRenewationType()).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.registrationRenewationType();
        assertEquals(expectedResult, result);
        verify(masterDataService).registrationRenewationType();
    }

    @Test
    void testFacilityType() {
        when(masterDataService.facilityType()).thenReturn(expectedResult);
        List<MasterDataTO> result = masterDataController.facilityType();
        assertEquals(expectedResult, result);
        verify(masterDataService).facilityType();
    }

/*    @Test
    void testFacilityTypeNegative() {
        List<MasterDataTO> unexpectedResult = Arrays.asList(
                new MasterDataTO(1l, "type1", "value1"),
                new MasterDataTO(2l,"type2", "value2")
        );
        when(masterDataService.facilityType()).thenReturn(unexpectedResult);
        //List<MasterDataTO> result = masterDataController.facilityType();
        assertEquals(unexpectedResult, masterDataController.facilityType());
        verify(masterDataService).facilityType();
    }*/

}