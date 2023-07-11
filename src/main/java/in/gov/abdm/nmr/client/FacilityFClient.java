package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.FacilitiesSearchResponseTO;
import in.gov.abdm.nmr.dto.FacilityRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Feign client for accessing the Facility Service.
 */
@FeignClient(name = FACILITY_SERVICE, url = GLOBAL_FACILITY_ENDPOINT)
public interface FacilityFClient {
    /**
     * Endpoint for searching facilities in facility service.
     *
     * @param authorization           the authorization header value
     * @param facilityRequestTO the facility search request payload
     * @return the FacilitySearchResponseTO search response Object
     */
    @PostMapping(value = FACILITY_SERVICE_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FacilitiesSearchResponseTO searchFacility(@RequestHeader(value = "Authorization") String authorization, @RequestBody FacilityRequestTO facilityRequestTO);

    @PostMapping(value = FETCH_FACILITY_INFO, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FacilitySearchResponseTO fetchFacilityInfo(@RequestHeader(value = "Authorization") String authorization, @RequestBody FacilityRequestTO facilityRequestTO);

}
