package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.FacilityFClient;
import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.service.impl.FacilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

    @InjectMocks
    FacilityServiceImpl facilityService;
    @Mock
    private FacilityFClient facilityFClient;
    @Mock
    GatewayFClient gatewayFClient;

    @Value("${session.clientId}")
    private String clientId;
    @Value("${session.clientSecret}")
    private String clientSecret;

    @BeforeEach
    void setup() {
    }

/*    @Test
    void testFindFacility() {
        when(gatewayFClient.sessions(any(SessionRequestTo.class))).thenReturn(getSessionResponse());
        when(facilityFClient.findFacility(anyString(), any(FacilitySearchRequestTO.class))).thenReturn(getFacilitySearchResponseTO());
        FacilitySearchResponseTO facility = facilityService.findFacility(any(FacilitySearchRequestTO.class));
        assertEquals(1, facility.getTotalFacilities());
    }*/
}