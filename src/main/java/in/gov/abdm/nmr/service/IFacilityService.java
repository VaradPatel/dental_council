package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FacilitiesSearchResponseTO;
import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

public interface IFacilityService {

    FacilitiesSearchResponseTO findFacility(FacilitySearchRequestTO facilitySearchRequestTo) throws InvalidRequestException;
}
