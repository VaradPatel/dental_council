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

/**
 * Presentation Layer to expose the endpoints of Health Professional Track Applications
 */
@RestController
@RequestMapping(PATH_HEALTH_PROFESSIONAL_ROOT)
public class TrackController {

    /**
     * Injecting a ITrackApplicationService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    ITrackApplicationService iTrackApplicationService;

    /**
     * Endpoint for retrieving information about a health professional's application requests to track by health professional.
     *
     * @param requestTO - HealthProfessionalApplicationRequestTo object passed as a request body
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @PostMapping(PATH_HEALTH_PROFESSIONAL_APPLICATIONS)
    public HealthProfessionalApplicationResponseTo trackApplicationDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetailsForHealthProfessional(requestTO);
    }

    /**
     * Endpoint for retrieving information about the track status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param requestTO - HealthProfessionalApplicationRequestTo object passed as a request body
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @PostMapping(PATH_TRACK_APPLICATIONS_STATUS)
    public HealthProfessionalApplicationResponseTo trackStatusDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetails(requestTO);
    }
}
