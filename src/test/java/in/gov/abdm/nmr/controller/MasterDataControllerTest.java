package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IMasterDataService;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SearchController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MasterDataController.class)
@ActiveProfiles(profiles = "local")
@SuppressWarnings("java:S1192")
class MasterDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMasterDataService masterDataService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void testGetStateMedicalCouncilsReturnsListOfCouncils() throws Exception {
        when(masterDataService.smcs())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get(NMRConstants.STATE_MEDICAL_COUNCIL).with(user("123")).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testSpecialities() throws Exception {
        when(masterDataService.specialities())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/specialities").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testCountries() throws Exception {
        when(masterDataService.countries())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/countries").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testStates() throws Exception {
        when(masterDataService.states(any(BigInteger.class)))
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/countries/1/states").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }


    @Test
    void testDistricts() throws Exception {
        when(masterDataService.districts(any(BigInteger.class)))
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/countries/states/1/districts").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }


    @Test
    void testSubDistricts() throws Exception {
        when(masterDataService.subDistricts(any(BigInteger.class)))
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/countries/states/districts/1/sub_districts").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testCities() throws Exception {
        when(masterDataService.cities(any(BigInteger.class)))
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/countries/states/districts/sub-districts/1/cities").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }


    @Test
    void testLanguages() throws Exception {
        when(masterDataService.languages())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/languages").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testCourses() throws Exception {
        when(masterDataService.courses())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/courses").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }

    @Test
    void testRegistrationRenewationType() throws Exception {
        when(masterDataService.registrationRenewationType())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/renewation-types").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }


    @Test
    void testFacilityType() throws Exception {
        when(masterDataService.facilityType())
                .thenReturn(IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(
                        IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(
                                List.of(CommonTestData.getStateMedicalCouncil()))));
        mockMvc.perform(get("/facility-types").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL));
    }
}