package in.gov.abdm.nmr.controller;

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_COUNT_ON_CARD_URL;

import in.gov.abdm.nmr.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.RequestTO;
import in.gov.abdm.nmr.dto.ResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

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
    private IDashboardService iDashboardService;

    /**
     * This endpoint can be accessed to retrieve the count of applications according to their status
     * @return ResponseTO
     */
    @PostMapping(FETCH_COUNT_ON_CARD_URL)
    public ResponseEntity<ResponseTO> fetchCountOnCard(@RequestBody RequestTO requestTO) throws InvalidRequestException {
        return ResponseEntity.ok(iDashboardService.fetchCountOnCard(requestTO.getUserType(), requestTO.getUserSubType()));
    }

}
