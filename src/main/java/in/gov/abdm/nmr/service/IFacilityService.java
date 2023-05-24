package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

public interface IFacilityService {

    FacilitySearchResponseTO findFacility(FacilitySearchRequestTO facilitySearchResponseTO) throws InvalidRequestException;
}
