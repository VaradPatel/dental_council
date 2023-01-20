package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.FacilityFClient;
import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import in.gov.abdm.nmr.service.IFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl implements IFacilityService {
    @Autowired
    private FacilityFClient facilityFClient;
    @Value("${authorization.token}")
    private String authorization;

    @Override
    public FacilitySearchResponseTO findFacility(FacilitySearchRequestTO facilitySearchRequestTO) {
        return facilityFClient.findFacility(authorization, facilitySearchRequestTO);
    }
}
