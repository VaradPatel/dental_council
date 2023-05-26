package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.FacilityFClient;
import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl implements IFacilityService {
    @Autowired
    private FacilityFClient facilityFClient;
    @Value("${session.clientId}")
    private String clientId;
    @Value("${session.clientSecret}")
    private String clientSecret;
    @Autowired
    GatewayFClient gatewayFClient;
    @Override
    public FacilitySearchResponseTO findFacility(FacilitySearchRequestTO facilitySearchRequestTO) throws InvalidRequestException {
        SessionResponseTo sessionResponseTo = getSessionToken();
        String authorization = "Bearer " + sessionResponseTo.getAccessToken();
        try {
            return facilityFClient.findFacility(authorization, facilitySearchRequestTO);
        }catch (Exception e){
            throw new InvalidRequestException(NMRConstants.INVALID_FACILITY_DETAILS_MESSAGE);
        }
    }
    private SessionResponseTo getSessionToken() {
        SessionRequestTo sessionRequestTo = SessionRequestTo.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        return gatewayFClient.sessions(sessionRequestTo);
    }
}
