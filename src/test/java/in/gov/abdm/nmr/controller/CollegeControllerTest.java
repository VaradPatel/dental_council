package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.CollegeMasterDataTO;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.dto.CollegeResponseTo;
import in.gov.abdm.nmr.exception.NmrExceptionAdvice;
import in.gov.abdm.nmr.service.ICollegeService;
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
@WebMvcTest(value = CollegeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {NmrExceptionAdvice.class, CollegeController.class})
@ActiveProfiles(profiles = "local")
class CollegeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ICollegeService collegeServiceV2;
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
    void testGetAllColleges() throws Exception {
        List<CollegeMasterDataTO> collegeMasterDataTOES = new ArrayList<>();
        CollegeMasterDataTO collegeMasterDataTO = new CollegeMasterDataTO();
        collegeMasterDataTO.setId(ID);
        collegeMasterDataTO.setName(COLLEGE_NAME);
        collegeMasterDataTOES.add(collegeMasterDataTO);

        when(collegeServiceV2.getAllColleges()).thenReturn(collegeMasterDataTOES);
        mockMvc.perform(get("/colleges").with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(COLLEGE_NAME));
    }

    @Test
    @WithMockUser
    void testGetCollege() throws Exception {
        CollegeResponseTo collegeResponseTo = new CollegeResponseTo();
        collegeResponseTo.setId(ID);
        collegeResponseTo.setName(COLLEGE_NAME);
        collegeResponseTo.setStateId(STATE_ID);
        collegeResponseTo.setCourseId(COURSE_ID);
        collegeResponseTo.setCollegeCode(COLLEGE_CODE);
        collegeResponseTo.setWebsite(WEBSITE);
        collegeResponseTo.setAddressLine1(ADDRESS_LINE_1);
        collegeResponseTo.setAddressLine2(ADDRESS_LINE_2);
        collegeResponseTo.setDistrictId(DISTRICT_ID);
        collegeResponseTo.setVillageId(VILLAGE_ID);
        collegeResponseTo.setPinCode(PIN_CODE);
        collegeResponseTo.setStateMedicalCouncilId(SMC_ID);
        collegeResponseTo.setUniversityId(UNIVERSITY_ID);
        collegeResponseTo.setEmailId(EMAIL_ID);
        collegeResponseTo.setMobileNumber(MOBILE_NUMBER);
        when(collegeServiceV2.getCollege(any(BigInteger.class))).thenReturn(collegeResponseTo);
        mockMvc.perform(get("/colleges/1").with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(COLLEGE_NAME))
                .andExpect(jsonPath("$.state_id").value(STATE_ID))
                .andExpect(jsonPath("$.course_id").value(COURSE_ID))
                .andExpect(jsonPath("$.college_code").value(COLLEGE_CODE))
                .andExpect(jsonPath("$.website").value(WEBSITE))
                .andExpect(jsonPath("$.address_line1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.address_line2").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.district_id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.village_id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.pin_code").value(PIN_CODE))
                .andExpect(jsonPath("$.state_medical_council_id").value(SMC_ID))
                .andExpect(jsonPath("$.university_id").value(UNIVERSITY_ID))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_number").value(MOBILE_NUMBER));
    }

/*
    @Test
    @WithMockUser
    void testCreateCollege() throws Exception {
        CollegeResponseTo response = new CollegeResponseTo();
        response.setId(ID);
        response.setName(COLLEGE_NAME);
        response.setStateId(STATE_ID);
        response.setCourseId(COURSE_ID);
        response.setCollegeCode(COLLEGE_CODE);
        response.setWebsite(WEBSITE);
        response.setAddressLine1(ADDRESS_LINE_1);
        response.setAddressLine2(ADDRESS_LINE_2);
        response.setDistrictId(DISTRICT_ID);
        response.setVillageId(VILLAGE_ID);
        response.setPinCode(PIN_CODE);
        response.setStateMedicalCouncilId(SMC_ID);
        response.setUniversityId(UNIVERSITY_ID);
        response.setEmailId(EMAIL_ID);
        response.setMobileNumber(MOBILE_NUMBER);
        when(collegeServiceV2.createOrUpdateCollege(any(CollegeResponseTo.class))).thenReturn(response);
        mockMvc.perform(post("/colleges").with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(response))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(COLLEGE_NAME))
                .andExpect(jsonPath("$.stateId").value(STATE_ID))
                .andExpect(jsonPath("$.courseId").value(COURSE_ID))
                .andExpect(jsonPath("$.collegeCode").value(COLLEGE_CODE))
                .andExpect(jsonPath("$.website").value(WEBSITE))
                .andExpect(jsonPath("$.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.addressLine2").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.districtId").value(DISTRICT_ID))
                .andExpect(jsonPath("$.villageId").value(VILLAGE_ID))
                .andExpect(jsonPath("$.pinCode").value(PIN_CODE))
                .andExpect(jsonPath("$.stateMedicalCouncilId").value(SMC_ID))
                .andExpect(jsonPath("$.universityId").value(UNIVERSITY_ID))
                .andExpect(jsonPath("$.emailId").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobileNumber").value(MOBILE_NUMBER));
    }
*/


/*
    @Test
    @WithMockUser
    void testUpdateCollege() throws Exception {
        CollegeResponseTo collegeResponseTo = new CollegeResponseTo();
        collegeResponseTo.setName(COLLEGE_NAME);
        collegeResponseTo.setStateId(STATE_ID);
        collegeResponseTo.setCourseId(COURSE_ID);
        collegeResponseTo.setCollegeCode(COLLEGE_CODE);
        collegeResponseTo.setWebsite(WEBSITE);
        collegeResponseTo.setAddressLine1(ADDRESS_LINE_1);
        collegeResponseTo.setAddressLine2(ADDRESS_LINE_2);
        collegeResponseTo.setDistrictId(DISTRICT_ID);
        collegeResponseTo.setVillageId(VILLAGE_ID);
        collegeResponseTo.setPinCode(PIN_CODE);
        collegeResponseTo.setStateMedicalCouncilId(SMC_ID);
        collegeResponseTo.setUniversityId(UNIVERSITY_ID);
        collegeResponseTo.setEmailId(EMAIL_ID);
        collegeResponseTo.setMobileNumber(MOBILE_NUMBER);
        when(collegeServiceV2.createOrUpdateCollege(any(CollegeResponseTo.class))).thenReturn(collegeResponseTo);
        mockMvc.perform(put("/colleges/1").with(user(TEST_USER))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(collegeResponseTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(COLLEGE_NAME))
                .andExpect(jsonPath("$.stateId").value(STATE_ID))
                .andExpect(jsonPath("$.courseId").value(COURSE_ID))
                .andExpect(jsonPath("$.collegeCode").value(COLLEGE_CODE))
                .andExpect(jsonPath("$.website").value(WEBSITE))
                .andExpect(jsonPath("$.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.addressLine2").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.districtId").value(DISTRICT_ID))
                .andExpect(jsonPath("$.villageId").value(VILLAGE_ID))
                .andExpect(jsonPath("$.pinCode").value(PIN_CODE))
                .andExpect(jsonPath("$.stateMedicalCouncilId").value(SMC_ID))
                .andExpect(jsonPath("$.universityId").value(UNIVERSITY_ID))
                .andExpect(jsonPath("$.emailId").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobileNumber").value(MOBILE_NUMBER));
    }
*/

    @Test
    void testGetAllCollegeVerifiersDesignation() throws Exception {
        List<CollegeMasterDataTO> collegeMasterDataTOES = new ArrayList<>();
        CollegeMasterDataTO collegeMasterDataTO = new CollegeMasterDataTO(ID, COLLEGE_NAME);
        collegeMasterDataTOES.add(collegeMasterDataTO);

        when(collegeServiceV2.getAllCollegeVerifiersDesignation()).thenReturn(collegeMasterDataTOES);
        mockMvc.perform(get("/colleges/verifiers/designations").with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id").value(ID))
                .andExpect(jsonPath("$.[0]name").value(COLLEGE_NAME));
    }

    /*
        @Test
        @WithMockUser
        void testUpdateCollegeVerifier() throws Exception {
            CollegeProfileTo collegeProfileTo = new CollegeProfileTo();
            collegeProfileTo.setId(ID);
            collegeProfileTo.setCollegeId(COLLEGE_ID);
            collegeProfileTo.setDesignation(DESIGNATION);
            collegeProfileTo.setName(COLLEGE_NAME);
            collegeProfileTo.setMobileNumber(MOBILE_NUMBER);
            collegeProfileTo.setEmailId(EMAIL_ID);
            when(collegeServiceV2.createOrUpdateCollegeVerifier(any(CollegeProfileTo.class))).thenReturn(collegeProfileTo);
            mockMvc.perform(put("/colleges/1/verifiers/1").with(user(TEST_USER))
                            .with(csrf())
                            .content(objectMapper.writeValueAsBytes(collegeProfileTo))
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ID))
                    .andExpect(jsonPath("$.collegeId").value(COLLEGE_ID))
                    .andExpect(jsonPath("$.designation").value(DESIGNATION))
                    .andExpect(jsonPath("$.name").value(COLLEGE_NAME))
                    .andExpect(jsonPath("$.emailId").value(EMAIL_ID))
                    .andExpect(jsonPath("$.mobileNo").value(MOBILE_NUMBER));
        }
    */
    @Test
    void testGetCollegeVerifier() throws Exception {
        CollegeProfileTo collegeProfileTo = new CollegeProfileTo();
        collegeProfileTo.setId(ID);
        collegeProfileTo.setCollegeId(COLLEGE_ID);
        collegeProfileTo.setDesignation(DESIGNATION);
        collegeProfileTo.setName(COLLEGE_NAME);
        collegeProfileTo.setMobileNumber(MOBILE_NUMBER);
        collegeProfileTo.setEmailId(EMAIL_ID);
        when(collegeServiceV2.getCollegeVerifier(any(BigInteger.class), any(BigInteger.class))).thenReturn(collegeProfileTo);
        mockMvc.perform(get("/colleges/1/verifiers/1").with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.college_id").value(COLLEGE_ID))
                .andExpect(jsonPath("$.designation").value(DESIGNATION))
                .andExpect(jsonPath("$.name").value(COLLEGE_NAME))
                .andExpect(jsonPath("$.mobile_number").value(MOBILE_NUMBER))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID));
    }
}