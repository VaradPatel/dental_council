package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.RequestTO;
import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static in.gov.abdm.nmr.api.constant.NMRConstants.*;

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
     *
     * @return ResponseTO
     */
    @PostMapping(FETCH_COUNT_ON_CARD_URL)
    public ResponseEntity<ResponseTO> fetchCountOnCard(@RequestBody RequestTO requestTO) throws InvalidRequestException {
        return ResponseEntity.ok(dashboardService.fetchCountOnCard(requestTO.getUserType(), requestTO.getUserSubType()));
    }

}
