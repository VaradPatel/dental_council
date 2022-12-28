package in.gov.abdm.nmr.api.controller.dashboard;

import static in.gov.abdm.nmr.api.constant.NMRConstants.FETCH_COUNT_ON_CARD_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;

/**
 * Presentation Layer to expose the endpoints of Dashboard
 */
@RestController
public class DashboardController {

    /**
     * Injecting a Dashboard Service bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private DashboardService dashboardService;

    /**
     * This endpoint can be accessed to retrieve the count of applications according to their status
     * @return ResponseTO
     */
    @GetMapping(path = FETCH_COUNT_ON_CARD_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseTO> fetchCountOnCard(){
        return new ResponseEntity<>(dashboardService.fetchCountOnCard(), HttpStatus.OK);
    }

}
