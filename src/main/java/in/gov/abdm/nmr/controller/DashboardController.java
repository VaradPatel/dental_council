package in.gov.abdm.nmr.controller;

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_COUNT_ON_CARD_URL;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_SPECIFIC_DETAILS_URL;

import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.util.List;

/**
 * Presentation Layer to expose the endpoints of Dashboard
 */
@RestController
public class DashboardController {

    /**
     * Injecting a FetchCountOnCardService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchCountOnCardService iFetchCountOnCardService;

    /**
     * Injecting a FetchSpecificDetailsService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchSpecificDetailsService iFetchSpecificDetailsService;

    /**
     * This endpoint can be accessed to retrieve the count of applications according to their status
     *
     * @return ResponseTO
     */
    @PostMapping(FETCH_COUNT_ON_CARD_URL)
    public ResponseEntity<FetchCountOnCardResponseTO> fetchCountOnCard(@RequestBody DashboardRequestTO requestTO) throws InvalidRequestException {
        return ResponseEntity.ok(iFetchCountOnCardService.fetchCountOnCard(requestTO.getUserType(), requestTO.getUserSubType()));
    }

    /**
     * This endpoint can be accessed to retrieve specific details based on the card selected
     * @return ResponseTO
     */
    @PostMapping(FETCH_SPECIFIC_DETAILS_URL)
    public ResponseEntity<List<FetchSpecificDetailsResponseTO>> fetchSpecificDetails(@RequestBody DashboardRequestTO requestTO) throws InvalidRequestException {
        return ResponseEntity.ok(iFetchSpecificDetailsService.fetchSpecificDetails(requestTO.getUserType(),
                requestTO.getUserSubType(),
                requestTO.getAppStatusType(),
                requestTO.getHpProfileStatus()));
    }

}
