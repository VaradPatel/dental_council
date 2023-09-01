package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.FacilityFClient;
import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.FacilitiesSearchResponseTO;
import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilityRequestTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.service.impl.FacilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

    public static FacilitySearchRequestTO getFacilitySearchRequest() {
        FacilitySearchRequestTO facilitySearchRequestTO = new FacilitySearchRequestTO();
        facilitySearchRequestTO.setOwnership(OWNERSHIP);
        facilitySearchRequestTO.setState(STATE_NAME);
        facilitySearchRequestTO.setDistrict(DISTRICT_NAME);
        return facilitySearchRequestTO;
    }

    @Test
    void testFindFacilityShouldFindFacility() throws InvalidRequestException {
        Mockito.when(gatewayFClient.sessions(any(SessionRequestTo.class))).thenReturn(getSessionResponse());
        when(facilityFClient.searchFacility(anyString(), any(FacilityRequestTO.class))).thenReturn(getFacilitySearchResponseTO());
        FacilitiesSearchResponseTO facility = facilityService.findFacility(getFacilitySearchRequest());
    }

    @Test
    void testFindFacilityShouldThrowInvalidRequestException() {
        Mockito.when(gatewayFClient.sessions(any(SessionRequestTo.class))).thenReturn(getSessionResponse());
        assertThrows(InvalidRequestException.class, () -> facilityService.findFacility(new FacilitySearchRequestTO()));
    }
}