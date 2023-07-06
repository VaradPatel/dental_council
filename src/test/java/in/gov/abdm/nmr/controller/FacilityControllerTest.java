package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NmrExceptionAdvice;
import in.gov.abdm.nmr.service.IFacilityService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.CommonTestData.FACILITY_ID;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FacilityController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {NmrExceptionAdvice.class, FacilityController.class})
@ActiveProfiles(profiles = "local")
class FacilityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IFacilityService facilityService;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser
    void testSearchFacility() throws Exception {
        FacilityRequestTO facilityRequestTO = new FacilityRequestTO();
        facilityRequestTO.getFacility().setId(FACILITY_ID);
        facilityRequestTO.getFacility().setOwnership(OWNERSHIP_CODE);
        facilityRequestTO.getFacility().setState(STATE_CODE);
        facilityRequestTO.getFacility().setDistrict(SUB_DISTRICT_CODE);

        FacilitiesSearchResponseTO facilitySearchResponseTO = new FacilitiesSearchResponseTO();
        facilitySearchResponseTO.setReferenceNumber(SUCCESS_RESPONSE);
        List<FacilityTO> facilities = new ArrayList<>();
        FacilityTO facilityTO = new FacilityTO();
        facilityTO.setId(FACILITY_ID);
        facilityTO.setName(FACILITY_NAME);
        facilityTO.setLongitude(STATE_CODE);
        facilityTO.setLatitude(DISTRICT_NAME);
        facilityTO.setLatitude(LATITUDE);
        facilityTO.setLongitude(LONGITUDE);
        facilityTO.setSystemOfMedicine(SYSTEM_OF_MEDICINE_CODE);
        facilityTO.setContactNumber(FACILITY_CODE);
        facilityTO.setFacilityProfileStatus(FACILITY_STATUS);
        facilityTO.setFacilityType(FACILITY_TYPE);
        facilities.add(facilityTO);
        facilitySearchResponseTO.setFacilities(facilities);
        when(facilityService.findFacility(nullable(FacilitySearchRequestTO.class))).thenReturn(facilitySearchResponseTO);
        mockMvc.perform(post(PATH_FACILITY_ROOT + PATH_FACILITY_SEARCH).with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(facilityRequestTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE))
                .andExpect(jsonPath("$.facilities[0].facilityId").value(FACILITY_ID))
                .andExpect(jsonPath("$.facilities[0].facilityName").value(FACILITY_NAME))
                .andExpect(jsonPath("$.facilities[0].ownership").value(OWNERSHIP))
                .andExpect(jsonPath("$.facilities[0].ownershipCode").value(OWNERSHIP_CODE))
                .andExpect(jsonPath("$.facilities[0].stateName").value(STATE_NAME))
                .andExpect(jsonPath("$.facilities[0].stateLGDCode").value(STATE_CODE))
                .andExpect(jsonPath("$.facilities[0].districtName").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.facilities[0].subDistrictName").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.facilities[0].villageCityTownName").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.facilities[0].districtLGDCode").value(DISTRICT_CODE))
                .andExpect(jsonPath("$.facilities[0].subDistrictLGDCode").value(SUB_DISTRICT_CODE))
                .andExpect(jsonPath("$.facilities[0].villageCityTownLGDCode").value(VILLAGE_CODE))
                .andExpect(jsonPath("$.facilities[0].address").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.facilities[0].pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.facilities[0].latitude").value(LATITUDE))
                .andExpect(jsonPath("$.facilities[0].longitude").value(LONGITUDE))
                .andExpect(jsonPath("$.facilities[0].systemOfMedicineCode").value(SYSTEM_OF_MEDICINE_CODE))
                .andExpect(jsonPath("$.facilities[0].systemOfMedicine").value(SYSTEM_OF_MEDICINE_CODE))
                .andExpect(jsonPath("$.facilities[0].facilityTypeCode").value(FACILITY_CODE))
                .andExpect(jsonPath("$.facilities[0].facilityStatus").value(FACILITY_STATUS))
                .andExpect(jsonPath("$.facilities[0].facilityType").value(FACILITY_TYPE))
                .andExpect(jsonPath("$.totalFacilities").value(1))
                .andExpect(jsonPath("$.numberOfPages").value(1));
    }
}