package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.service.ISearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import java.awt.print.Pageable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.SALUTATION_DR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SearchController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = SearchController.class)
@ActiveProfiles(profiles = "local")
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISearchService searchService;

    @Autowired
    private WebApplicationContext context;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void testGetHpSearchProfileByIdShouldReturnHealthProfessionalDetails() throws Exception {
        HpSearchProfileTO hpSearchProfileTO = new HpSearchProfileTO();
        hpSearchProfileTO.setProfilePhoto(PROFILE_PHOTO);
        hpSearchProfileTO.setDateOfBirth(DATE_OF_BIRTH.toString());
        hpSearchProfileTO.setEmail(EMAIL_ID);
        hpSearchProfileTO.setFullName(PROFILE_DISPLAY_NAME);
        hpSearchProfileTO.setDateOfRegistration(REGISTRATION_DATE.toString());
        hpSearchProfileTO.setSalutation(SALUTATION_DR);
        hpSearchProfileTO.setQualifications(Collections.emptyList());
        hpSearchProfileTO.setFatherHusbandName(FIRST_NAME);
        hpSearchProfileTO.setStateMedicalCouncil(STATE_MEDICAL_COUNCIL);
        hpSearchProfileTO.setRegistrationYear(REGISTRATION_YEAR);
        hpSearchProfileTO.setYearOfInfo(REGISTRATION_YEAR);
        hpSearchProfileTO.setRegistrationNumber(REGISTRATION_NUMBER);
        hpSearchProfileTO.setMobileNumber(MOBILE_NUMBER);
        hpSearchProfileTO.setNmrId(NMR_ID);
        when(searchService.getHpSearchProfileById(any(BigInteger.class))).thenReturn(hpSearchProfileTO);
        mockMvc.perform(get("/health-professional/1").with(user("123")).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.state_medical_council").value(STATE_MEDICAL_COUNCIL))
                .andExpect(jsonPath("$.email").value(EMAIL_ID))
                .andExpect(jsonPath("$.profile_photo").value(PROFILE_PHOTO))
                .andExpect(jsonPath("$.date_of_registration").value(REGISTRATION_DATE.toString()))
                .andExpect(jsonPath("$.nmr_id").value(NMR_ID))
                .andExpect(jsonPath("$.registration_number").value(REGISTRATION_NUMBER))
                .andExpect(jsonPath("$.mobile_number").value(MOBILE_NUMBER));
    }

    @Test
    void testSearchHP() throws Exception {
        HpSearchRequestTO searchRequestTO = new HpSearchRequestTO();
        searchRequestTO.setFullName(PROFILE_DISPLAY_NAME);
        searchRequestTO.setRegistrationNumber(REGISTRATION_NUMBER);
        searchRequestTO.setRegistrationYear(REGISTRATION_YEAR);
        searchRequestTO.setStateMedicalCouncilId(SMC_ID);
        searchRequestTO.setProfileStatusId(Arrays.asList());
        HpSearchResponseTO responseTO = new HpSearchResponseTO();
        List<HpSearchResultTO> results = new ArrayList<>();
        HpSearchResultTO resultTO = new HpSearchResultTO();
        resultTO.setProfileId(ID);
        resultTO.setFullName(PROFILE_DISPLAY_NAME);
        resultTO.setSalutation(SALUTATION_DR);
        resultTO.setRegistrationNumber(REGISTRATION_NUMBER);
        resultTO.setRegistrationYear(REGISTRATION_YEAR);
        resultTO.setStateMedicalCouncil(STATE_MEDICAL_COUNCIL);
        resultTO.setProfilePhoto(PROFILE_PHOTO);
        results.add(resultTO);
        responseTO.setResults(results);
        responseTO.setCount(1L);
        when(searchService.searchHP(any(HpSearchRequestTO.class), (org.springframework.data.domain.Pageable) any(Pageable.class))).thenReturn(responseTO);
        mockMvc.perform(get("/health-professional/search")
                        .with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(new HpSearchRequestTO()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}