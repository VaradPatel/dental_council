package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.service.ISearchService;
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
import java.util.Collections;

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
@WebMvcTest(value = SearchController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ContextConfiguration(classes = SearchController.class)
@ActiveProfiles(profiles = "local")
@EnableWebMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISearchService searchService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void testGetHpSearchProfileByIdShouldReturnHealthProfessionalDetails() throws Exception {
        HpSearchProfileTO hpSearchProfileTO =  new HpSearchProfileTO();
        hpSearchProfileTO.setProfilePhoto(PROFILE_PHOTO);
        hpSearchProfileTO.setDateOfBirth(DATE_OF_BIRTH.toString());
        hpSearchProfileTO.setEmail(EMAIL_ID);
        hpSearchProfileTO.setFullName(PROFILE_DISPLAY_NAME);
        hpSearchProfileTO.setDateOfRegistration(REGISTRATION_DATE.toString());
        hpSearchProfileTO.setSalutation(NMRConstants.SALUTATION_DR);
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
                .andExpect(jsonPath("$.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.salutation").value(NMRConstants.SALUTATION_DR))
                .andExpect(jsonPath("$.stateMedicalCouncil").value(STATE_MEDICAL_COUNCIL))
                .andExpect(jsonPath("$.email").value(EMAIL_ID))
                .andExpect(jsonPath("$.profilePhoto").value(PROFILE_PHOTO))
                .andExpect(jsonPath("$.dateOfRegistration").value(REGISTRATION_DATE.toString()))
                .andExpect(jsonPath("$.nmrId").value(NMR_ID))
                .andExpect(jsonPath("$.registrationNumber").value(REGISTRATION_NUMBER))
                .andExpect(jsonPath("$.mobileNumber").value(MOBILE_NUMBER));

    }
}
