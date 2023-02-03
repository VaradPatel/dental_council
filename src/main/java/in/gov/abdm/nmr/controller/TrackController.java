package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.service.ITrackApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-professional")
public class TrackController {

    @Autowired
    ITrackApplicationService iTrackApplicationService;

    @PostMapping("/applications")
    public HealthProfessionalApplicationResponseTo trackApplicationDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetailsForHealthProfessional(requestTO);
    }

    @PostMapping("/applications/status")
    public HealthProfessionalApplicationResponseTo trackStatusDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return iTrackApplicationService.fetchApplicationDetails(requestTO);
    }
}
