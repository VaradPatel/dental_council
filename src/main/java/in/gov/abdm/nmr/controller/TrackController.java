package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.service.ITrackApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_HEALTH_PROFESSIONAL_APPLICATIONS;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_HEALTH_PROFESSIONAL_ROOT;
import static in.gov.abdm.nmr.util.NMRConstants.PATH_TRACK_APPLICATIONS_STATUS;

@RestController
@RequestMapping(PATH_HEALTH_PROFESSIONAL_ROOT)
public class TrackController {

    @Autowired
    ITrackApplicationService iTrackApplicationService;

    @PostMapping(PATH_HEALTH_PROFESSIONAL_APPLICATIONS)
    public HealthProfessionalApplicationResponseTo trackApplicationDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetailsForHealthProfessional(requestTO);
    }

    @PostMapping(PATH_TRACK_APPLICATIONS_STATUS)
    public HealthProfessionalApplicationResponseTo trackStatusDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetails(requestTO);
    }
}
