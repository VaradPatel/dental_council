package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Client to call Facility Service API
 */
@FeignClient(name = FACILITY_SERVICE, url = GLOBAL_FACILITY_ENDPOINT)
public interface FacilityFClient {
    @PostMapping(value = FACILITY_SERVICE_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FacilitySearchResponseTO findFacility(@RequestHeader(value = "Authorization") String authorization, @RequestBody FacilitySearchRequestTO facilitySearchRequestTO);
}
