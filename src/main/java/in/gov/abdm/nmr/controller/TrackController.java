package in.gov.abdm.nmr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_HEALTH_PROFESSIONAL_ROOT;

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



}
