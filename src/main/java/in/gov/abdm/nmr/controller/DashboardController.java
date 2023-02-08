package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchDetailsByRegNoService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_FETCH_DETAILS;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_ROOT;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_COUNT_ON_CARD_URL;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TRACK_APP_URL;

/**
 * Presentation Layer to expose the endpoints of Dashboard
 */
@RestController
@RequestMapping(PATH_DASHBOARD_ROOT)
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
     * Injecting a IFetchDetailsByRegNoService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchDetailsByRegNoService iFetchDetailsByRegNoService;

    /**
     * This endpoint can be accessed to retrieve the count of applications according to their status
     *
     * @return FetchCountOnCardResponseTO
     */
    @GetMapping(FETCH_COUNT_ON_CARD_URL)
    public ResponseEntity<FetchCountOnCardResponseTO> fetchCountOnCard() throws InvalidRequestException {
        return ResponseEntity.ok(iFetchCountOnCardService.fetchCountOnCard());
    }

    /**
     * This endpoint can be accessed to retrieve specific details based on the card selected
     * @return List<FetchSpecificDetailsResponseTO>
     */
//    @PostMapping(FETCH_SPECIFIC_DETAILS_URL)
//    public ResponseEntity<List<FetchSpecificDetailsResponseTO>> fetchSpecificDetails(@RequestBody FetchSpecificDetailsRequestTO requestTO) throws InvalidRequestException {
//        return ResponseEntity.ok(iFetchSpecificDetailsService.fetchSpecificDetails(requestTO.getGroupName(), requestTO.getApplicationType(), requestTO.getWorkFlowStatus()));
//    }

    /**
     * This endpoint can be accessed to retrieve specific details based on the Registration Number
     * @return List<FetchSpecificDetailsResponseTO>
     */
//    @PostMapping(FETCH_DETAILS_BY_REG_NO_URL)
//    public ResponseEntity<List<FetchSpecificDetailsResponseTO>> fetchDetailsByRegNo(@RequestBody FetchDetailsByRegNoRequestTO requestTO) throws InvalidRequestException {
//        return ResponseEntity.ok(iFetchDetailsByRegNoService.fetchDetailsByRegNo(requestTO.getRegistrationNumber(),
//                requestTO.getSmcName(),
//                requestTO.getUserType(),
//                requestTO.getUserSubType()));
//    }

    @PostMapping(PATH_DASHBOARD_FETCH_DETAILS)
    public DashboardResponseTO DashBoardResponseTO(@RequestBody DashboardRequestTO requestTO) throws InvalidRequestException {
        return iFetchSpecificDetailsService.fetchDashboardData(requestTO);
    }

    @PostMapping(FETCH_TRACK_APP_URL)
    public ResponseEntity<List<FetchTrackApplicationResponseTO>> fetchTrackApplicationDetails(@RequestBody FetchTrackApplicationRequestTO requestTO) {
        return new ResponseEntity<>(iFetchSpecificDetailsService.fetchTrackApplicationDetails(requestTO), HttpStatus.ACCEPTED);
    }
}