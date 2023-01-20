package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.dto.FacilitySearchResponseTO;
import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(NMRConstants.PATH_FACILITY_ROOT)
public class FacilityController {
    @Autowired
    private IFacilityService facilityService;

    @PostMapping(path = NMRConstants.PATH_FACILITY_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FacilitySearchResponseTO searchFacility(@RequestBody FacilitySearchRequestTO facilitySearchRequestTO) {
        return facilityService.findFacility(facilitySearchRequestTO);
    }
}
