package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.service.IMasterDataService;
import in.gov.abdm.nmr.util.CommonTestData;
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

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.ID;
import static in.gov.abdm.nmr.util.CommonTestData.STATE_MEDICAL_COUNCIL;
import static in.gov.abdm.nmr.util.NMRConstants.STATE_MEDICAL_COUNCIL_URL;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SearchController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ContextConfiguration(classes = MasterDataController.class)
@ActiveProfiles(profiles = "local")
@EnableWebMvc
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
        mockMvc.perform(get(STATE_MEDICAL_COUNCIL_URL).with(user("123")).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(STATE_MEDICAL_COUNCIL)) ;
    }
}