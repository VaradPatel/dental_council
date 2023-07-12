package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.FacilityFClient;
import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.District;
import in.gov.abdm.nmr.entity.State;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.DistrictRepository;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private IStateRepository stateRepository;

    @Override
    public FacilitiesSearchResponseTO findFacility(FacilitySearchRequestTO facilitySearchRequestTo) throws InvalidRequestException {

        SessionResponseTo sessionResponseTo = getSessionToken();
        String authorization = "Bearer " + sessionResponseTo.getAccessToken();

        FacilityRequestTO facilityRequestTO = new FacilityRequestTO();
        facilityRequestTO.setFacility(facilitySearchRequestTo);
        facilityRequestTO.setRequestId(UUID.randomUUID().toString());
        facilityRequestTO.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        if (facilitySearchRequestTo.getId() != null) {

            FacilitySearchResponseTO facilitySearchResponseTO = facilityFClient.fetchFacilityInfo(authorization, facilityRequestTO);

            FacilitiesSearchResponseTO facilitiesSearchResponseTO = new FacilitiesSearchResponseTO();
            facilitiesSearchResponseTO.setReferenceNumber(facilitySearchResponseTO.getReferenceNumber());
            facilitiesSearchResponseTO.setFacilities(List.of(facilitySearchResponseTO.getFacility()));

            if (StringUtils.isNotBlank(facilitySearchResponseTO.getFacility().getFacilityAddressTo().getDistrict())) {
                District districtByIsoCode = districtRepository.getDistrictByIsoCode(
                        facilitySearchResponseTO.getFacility().getFacilityAddressTo().getDistrict());

                facilitySearchResponseTO.getFacility().getFacilityAddressTo().setDistrictTO(DistrictTO.builder()
                        .id(districtByIsoCode.getId()).name(districtByIsoCode.getName()).build());
            }

            if (StringUtils.isNotBlank(facilitySearchResponseTO.getFacility().getFacilityAddressTo().getState())) {
                State stateByIsoCode = stateRepository.getStateByIsoCode(
                        facilitySearchResponseTO.getFacility().getFacilityAddressTo().getState());

                facilitySearchResponseTO.getFacility().getFacilityAddressTo().setStateTO(
                        StateTO.builder().id(stateByIsoCode.getId()).name(stateByIsoCode.getName()).build());
            }
            return facilitiesSearchResponseTO;

        } else if (facilitySearchRequestTo.getOwnership() != null && facilitySearchRequestTo.getState() != null && facilitySearchRequestTo.getDistrict() != null) {

            return facilityFClient.searchFacility(authorization, facilityRequestTO);

        } else {

            throw new InvalidRequestException(NMRConstants.INVALID_FACILITY_PAYLOAD_MESSAGE);
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
