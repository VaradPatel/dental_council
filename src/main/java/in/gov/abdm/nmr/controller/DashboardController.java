package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_FETCH_DETAILS;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_ROOT;
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
     * This endpoint can be accessed to retrieve the count of applications according to their status
     *
     * @return FetchCountOnCardResponseTO
     */
    @GetMapping(ProtectedPaths.PATH_DASHBOARD_CARD_COUNT)
    @SecurityRequirement(name = "bearerAuth")
    public FetchCountOnCardResponseTO fetchCountOnCard() throws InvalidRequestException, AccessDeniedException {
        return iFetchCountOnCardService.fetchCountOnCard();
    }

    /**
     * This endpoint can be accessed to retrieve specific details based on the Registration Number
     * @return List<FetchSpecificDetailsResponseTO>
     */

    /**
     * This API endpoint is used to retrieve Dashboard specific request details.
     * @param requestTO - The request object containing the necessary parameters to fetch the dashboard details.
     * @return The response object with request specific details related to the dashboard.
     * @throws InvalidRequestException when the request is invalid.
     */
    @PostMapping(PATH_DASHBOARD_FETCH_DETAILS)
    public DashboardResponseTO DashBoardResponseTO(@RequestBody DashboardRequestTO requestTO) throws InvalidRequestException {
        return iFetchSpecificDetailsService.fetchDashboardData(requestTO);
    }

}