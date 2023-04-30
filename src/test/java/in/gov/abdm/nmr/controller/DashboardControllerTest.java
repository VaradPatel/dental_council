package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import in.gov.abdm.nmr.service.impl.FetchCountOnCardServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_HP_REGISTRATION_REQUESTS;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DashboardController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ContextConfiguration(classes = DashboardController.class)
@ActiveProfiles(profiles = "local")
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFetchCountOnCardService iFetchCountOnCardService;

    @MockBean
    private IFetchSpecificDetailsService iFetchSpecificDetailsService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser
    void testFetchCountOnCardServiceShouldReturnValidCounts() throws Exception {
        FetchCountOnCardResponseTO  fetchCountOnCardResponseTO = new FetchCountOnCardResponseTO();
        FetchCountOnCardInnerResponseTO fetchCountOnCardInnerResponseTO =  new FetchCountOnCardInnerResponseTO();
        fetchCountOnCardInnerResponseTO.setApplicationTypeIds(StringUtils.join(List.of(ApplicationType.HP_REGISTRATION.getId(), ApplicationType.FOREIGN_HP_REGISTRATION.getId())));
        fetchCountOnCardInnerResponseTO.setStatusWiseCount(getStatusWiseCount());
        fetchCountOnCardResponseTO.setHpRegistrationRequest(fetchCountOnCardInnerResponseTO);
        when(iFetchCountOnCardService.fetchCountOnCard()).thenReturn(fetchCountOnCardResponseTO);

        mockMvc.perform(get(ProtectedPaths.PATH_DASHBOARD_ROOT+ProtectedPaths.PATH_DASHBOARD_CARD_COUNT)
                        .with(user("123")).
                        accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[0].name").value("Total Registration Requests"))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[1].name").value("Pending"))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[2].name").value("Approved"))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[3].name").value("Query Raised"))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[4].name").value("Rejected"))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[0].count").value(BigInteger.valueOf(40)))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[1].count").value(BigInteger.TEN))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[2].count").value(BigInteger.TEN))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[3].count").value(BigInteger.TEN))
                        .andExpect(jsonPath("$.hp_registration_request.status_wise_count[4].count").value(BigInteger.TEN));

    }

    @Test
    @WithMockUser
    void testFetchCardDetailsShouldReturnValidResponse() throws Exception {
        DashboardResponseTO dashboardResponseTO = new DashboardResponseTO();
        DashboardTO dashboardTO  = new DashboardTO();
        dashboardTO.setApplicantFullName(PROFILE_DISPLAY_NAME);
        dashboardTO.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
        dashboardTO.setCollegeDeanStatus(WorkflowStatus.APPROVED.name());
        dashboardTO.setCollegeStatus(WorkflowStatus.APPROVED.name());
        dashboardTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        dashboardTO.setDoctorStatus(WorkflowStatus.APPROVED.name());
        dashboardTO.setEmailId(EMAIL_ID);
        dashboardTO.setHpProfileId(ID);
        dashboardTO.setMobileNumber(MOBILE_NUMBER);
        dashboardTO.setNmcStatus(WorkflowStatus.APPROVED.name());
        dashboardResponseTO.setTotalNoOfRecords(BigInteger.ONE);
        dashboardResponseTO.setDashboardTOList(List.of(dashboardTO));
        when(iFetchSpecificDetailsService.fetchCardDetails(nullable(String.class), nullable(String.class),nullable(String.class), nullable(String.class), nullable(String.class),nullable(Integer.class), nullable(Integer.class), nullable(String.class), nullable(String.class)))
                .thenReturn(dashboardResponseTO);




        mockMvc.perform(get(ProtectedPaths.PATH_DASHBOARD_ROOT + ProtectedPaths.PATH_DASHBOARD_FETCH_DETAILS)
                        .with(user("123"))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_no_of_records").value(BigInteger.ONE));

    }


    private static List<StatusWiseCountTO> getStatusWiseCount(){
        List<StatusWiseCountTO> defaultCards = FetchCountOnCardServiceImpl.getDefaultCards(TOTAL_HP_REGISTRATION_REQUESTS);
        defaultCards.forEach(statusWiseCountTO -> {
            if(statusWiseCountTO.getName().equals("Total Registration Requests")) {
                statusWiseCountTO.setCount(BigInteger.valueOf(40));
            }else{
                statusWiseCountTO.setCount(BigInteger.TEN);
            }
        });
        return defaultCards;

    }
}

