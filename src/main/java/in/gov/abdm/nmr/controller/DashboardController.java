package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_FETCH_DETAILS;
import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_DASHBOARD_ROOT;

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
    public DashboardResponseTO fetchDashboardData(@RequestBody DashboardRequestTO requestTO) throws InvalidRequestException {
        return iFetchSpecificDetailsService.fetchDashboardData(requestTO);
    }

    /**
     * Retrieves dashboard data based on the provided parameters.
     *
     * @param workFlowStatusId  the workflow status ID to filter by
     * @param applicationTypeId the application type ID to filter by
     * @param userGroupStatus   the user group status to filter by
     * @param smcId             the SMC ID to filter by
     * @param name              the name to filter by
     * @param nmrId             the NMR ID to filter by
     * @param search            the search string to filter by
     * @param pageNo            the page number to retrieve
     * @param size              the number of results per page
     * @param sortBy            the field to sort by
     * @param sortOrder         the sort order ("asc" or "desc")
     * @return a DashboardResponseTO object containing the retrieved data
     * @throws InvalidRequestException if the provided request parameters are invalid
     */
    @GetMapping(PATH_DASHBOARD_FETCH_DETAILS)
    public DashboardResponseTO fetchCardDetails(
            @RequestParam(required = false, value = "workFlowStatusId") String workFlowStatusId,
            @RequestParam(required = false, value = "applicationTypeId") String applicationTypeId,
            @RequestParam(required = false, value = "userGroupStatus") String userGroupStatus,
            @RequestParam(required = false, value = "smcId") String smcId,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "nmrId") String nmrId,
            @RequestParam(required = false, value = "search") String search,
            @RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(required = false, value = "size", defaultValue = "2") int size,
            @RequestParam(required = false, value = "sortBy") String sortBy,
            @RequestParam(required = false, value = "sortOrder") String sortOrder) throws InvalidRequestException {
        return iFetchSpecificDetailsService.fetchCardDetails(workFlowStatusId, applicationTypeId,
                userGroupStatus, smcId, name, nmrId, search, pageNo, size, sortBy, sortOrder);
    }

}