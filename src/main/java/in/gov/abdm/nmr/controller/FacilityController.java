package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.FacilitiesSearchResponseTO;
import in.gov.abdm.nmr.dto.FacilitySearchRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(NMRConstants.PATH_FACILITY_ROOT)
public class FacilityController {
    @Autowired
    private IFacilityService facilityService;

    @Autowired
    ChecksumUtil checksumUtil;

    @PostMapping(path = NMRConstants.PATH_FACILITY_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FacilitiesSearchResponseTO searchFacility(@RequestBody FacilitySearchRequestTO facilitySearchRequestTO) throws InvalidRequestException {
        checksumUtil.validateChecksum();
        return facilityService.findFacility(facilitySearchRequestTO);
    }

}
