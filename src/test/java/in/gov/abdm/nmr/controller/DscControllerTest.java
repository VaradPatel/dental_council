package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.dsc.*;
import in.gov.abdm.nmr.enums.TemplateEnum;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IDscService;
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

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DscController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ContextConfiguration(classes = DscController.class)
@ActiveProfiles(profiles = "local")
class DscControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IDscService dscService;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    void testInvokeDSCGenEspRequest() throws Exception {
        DscRequestTo dscRequestTo = new DscRequestTo();
        dscRequestTo.setTemplateId(TemplateEnum.TEMPLATE_1.name());
        dscRequestTo.setSigningPlace(SUB_DISTRICT_NAME);
        DocumentDetailsTO documentDetailsTO =  new DocumentDetailsTO();
        PersonalDetailTO personalDetailTO =  new PersonalDetailTO();
        personalDetailTO.setFullName(PROFILE_DISPLAY_NAME);
        personalDetailTO.setDob(DATE_OF_BIRTH.toString());
        personalDetailTO.setGender(GENDER);
        personalDetailTO.setMobileNumber(MOBILE_NUMBER);
        personalDetailTO.setNationality(NMRConstants.INDIA);
        documentDetailsTO.setPersonalDetail(personalDetailTO);
        PersonalCommunicationTO personalCommunicationTO = new PersonalCommunicationTO();
        personalCommunicationTO.setDistrict(DISTRICT_CODE);
        personalCommunicationTO.setState(STATE_NAME);
        personalCommunicationTO.setCountry(NMRConstants.INDIA);
        personalCommunicationTO.setPostalCode(PIN_CODE);
        documentDetailsTO.setPersonalCommunication(personalCommunicationTO);
        RegistrationDetailsTO registrationDetailsTO = new RegistrationDetailsTO();
        registrationDetailsTO.setRegistrationDate(REGISTRATION_DATE.toString());
        registrationDetailsTO.setRegistrationNumber(REGISTRATION_NUMBER);
        registrationDetailsTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        registrationDetailsTO.setRegistrationType(PERMANENT_RENEWATION);
        documentDetailsTO.setRegistrationDetailsTO(registrationDetailsTO);
        QualificationDetailsTO qualificationDetailsTO = new QualificationDetailsTO();
        qualificationDetailsTO.setCountry(NMRConstants.INDIA);
        qualificationDetailsTO.setState(STATE_NAME);
        qualificationDetailsTO.setCollege(COLLEGE_NAME);
        qualificationDetailsTO.setNameOfDegree(DEGREE);
        qualificationDetailsTO.setQualificationFrom(STATE_NAME);
        qualificationDetailsTO.setUniversity(UNIVERSITY_NAME);
        qualificationDetailsTO.setMonthAndYearOfAwarding(REGISTRATION_YEAR);
        documentDetailsTO.setQualificationDetailsTO(qualificationDetailsTO);

        DscResponseTo dscResponseTo = new DscResponseTo();
        dscResponseTo.setEspRequest("Request");
        dscResponseTo.setEspUrl("URL");
        dscResponseTo.setAspTxnId("TNX_ID");
        dscResponseTo.setContentType("HTML");
        when(dscService.invokeDSCGenEspRequest(dscRequestTo)).thenReturn(dscResponseTo);
        mockMvc.perform(post(ProtectedPaths.E_SIGN)
                        .with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(dscRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}