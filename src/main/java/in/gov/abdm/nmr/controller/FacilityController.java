package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.service.IFacilityService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(NMRConstants.PATH_FACILITY_ROOT)
public class FacilityController {
    @Autowired
    private IFacilityService facilityService;
}
