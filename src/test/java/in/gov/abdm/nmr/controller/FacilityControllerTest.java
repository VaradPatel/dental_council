package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.NmrExceptionAdvice;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IApplicationService;
import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.MESSAGE_SENDER;
import static in.gov.abdm.nmr.util.NMRConstants.STATE_MEDICAL_COUNCIL_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FacilityControllerTest.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {NmrExceptionAdvice.class, FacilityControllerTest.class})
@ActiveProfiles(profiles = "local")
@EnableWebMvc
public class FacilityControllerTest {

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
/*
    @Test
    @WithMockUser
    void testSearchFacility() throws Exception {
        FacilitySearchRequestTO facilitySearchRequestTO = new FacilitySearchRequestTO();
        facilitySearchRequestTO.setFacilityId("1");
        facilitySearchRequestTO.setFacilityName("a");
        facilitySearchRequestTO.setOwnershipCode("a");
        facilitySearchRequestTO.setStateLgdCode("a");
        facilitySearchRequestTO.setDistrictLgdCode(SUB_DISTRICT_CODE);
        facilitySearchRequestTO.setSubDistrictLgdCode(SUB_DISTRICT_CODE);
        facilitySearchRequestTO.setPincode(PIN_CODE);
        facilitySearchRequestTO.setPage(1);
        facilitySearchRequestTO.setResultsPerPage(1);

        FacilitySearchResponseTO facilitySearchResponseTO = new FacilitySearchResponseTO();
        facilitySearchResponseTO.setMessage(MESSAGE_SENDER);
        List<FacilityTO> facilities = new ArrayList<>();
        FacilityTO facilityTO = new FacilityTO();
        facilityTO.setFacilityId(STRING_ID);
        facilityTO.setFacilityName(COURSE_NAME);
        facilityTO.setOwnership("a");
        facilityTO.setOwnershipCode("a");
        facilityTO.setStateName("a");
        facilityTO.setStateLGDCode("a");
        facilityTO.setDistrictName("a");
        facilityTO.setSubDistrictName("a");
        facilityTO.setVillageCityTownName("a");
        facilityTO.setDistrictLGDCode("a");
        facilityTO.setSubDistrictLGDCode("a");
        facilityTO.setVillageCityTownLGDCode("a");
        facilityTO.setAddress(ADDRESS_LINE_1);
        facilityTO.setPincode(PIN_CODE);
        facilityTO.setLatitude(LATITUDE);
        facilityTO.setLongitude(LONGITUDE);
        facilityTO.setSystemOfMedicineCode("a");
        facilityTO.setSystemOfMedicine("a");
        facilityTO.setFacilityTypeCode("a");
        facilityTO.setFacilityStatus("a");
        facilityTO.setFacilityType("a");
        facilities.add(facilityTO);
        facilitySearchResponseTO.setFacilities(facilities);
        facilitySearchResponseTO.setTotalFacilities(1);
        facilitySearchResponseTO.setNumberOfPages(1);
        when(facilityService.findFacility(any(FacilitySearchRequestTO.class))).thenReturn(facilitySearchResponseTO);
        mockMvc.perform(post("/facilities/search").with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(facilitySearchResponseTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE))
                .andExpect(jsonPath("$[0].facilities.facilityId").value(COURSE_NAME))
                .andExpect(jsonPath("$[1].facilities.facilityName").value("a"))
                .andExpect(jsonPath("$[2].facilities.ownership").value("a"))
                .andExpect(jsonPath("$[3].facilities.ownershipCode").value("a"))
                .andExpect(jsonPath("$[4].facilities.stateName").value("a"))
                .andExpect(jsonPath("$[5].facilities.stateLGDCode").value("a"))
                .andExpect(jsonPath("$[6].facilities.districtName").value("a"))
                .andExpect(jsonPath("$[7].facilities.subDistrictName").value("a"))
                .andExpect(jsonPath("$[8].facilities.villageCityTownName").value("a"))
                .andExpect(jsonPath("$[9].facilities.districtLGDCode").value("a"))
                .andExpect(jsonPath("$[10].facilities.subDistrictLGDCode").value("a"))
                .andExpect(jsonPath("$[11].facilities.villageCityTownLGDCode").value("a"))
                .andExpect(jsonPath("$[12].facilities.address").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$[13].facilities.pincode").value(PIN_CODE))
                .andExpect(jsonPath("$[14].facilities.latitude").value(LATITUDE))
                .andExpect(jsonPath("$[15].facilities.longitude").value(LONGITUDE))
                .andExpect(jsonPath("$[16].facilities.systemOfMedicineCode").value("a"))
                .andExpect(jsonPath("$[17].facilities.systemOfMedicine").value("a"))
                .andExpect(jsonPath("$[18].facilities.facilityTypeCode").value("a"))
                .andExpect(jsonPath("$[19].facilities.facilityStatus").value("a"))
                .andExpect(jsonPath("$[20].facilities.facilityType").value("a"))
                .andExpect(jsonPath("$.totalFacilities").value(1))
                .andExpect(jsonPath("$.numberOfPages").value(1));
    }*/
}