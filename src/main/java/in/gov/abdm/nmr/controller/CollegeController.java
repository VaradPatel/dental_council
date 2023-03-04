package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.ICollegeService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigInteger;

@RestController
public class CollegeController {

    private ICollegeService collegeService;

    public CollegeController(ICollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping(path = "college/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo registerCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException, WorkFlowException {
        return collegeService.registerCollege(null, collegeRegistrationRequestTo, false);
    }

    /**
     * Updates the college with the given ID using the provided college registration request.
     * Requires bearer authentication.
     *
     * @param collegeId                    the ID of the college to update
     * @param collegeRegistrationRequestTo the college registration request containing the updated college information
     * @return the updated college profile
     * @throws NmrException      if there was an error with the request
     * @throws WorkFlowException if there was an error with the workflow
     */
    @PutMapping(path = ProtectedPaths.PATH_UPDATE_COLLEGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo updateCollege(@PathVariable("collegeId") BigInteger collegeId,
                                          @RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException, WorkFlowException {
        return collegeService.registerCollege(collegeId, collegeRegistrationRequestTo, true);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_REGISTRAR, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeRegistrarProfileTo registerRegistrar(@PathVariable("collegeId") BigInteger collegeId, @RequestBody CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        return collegeService.registerRegistrar(collegeId, collegeRegistrarCreationRequestTo);
    }

    /**
     * Updates the registrar profile for a specific college.
     *
     * @param collegeId                         the ID of the college to update the registrar profile for
     * @param registrarId                       the ID of the registrar to update the profile for
     * @param collegeRegistrarCreationRequestTo the registrar profile creation request
     * @return the updated college registrar profile
     * @throws NmrException if an error occurs while updating the registrar profile
     */
    @PutMapping(path = "/college/{collegeId}/registrar/{registrarId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeRegistrarProfileTo updateRegisterRegistrar(@PathVariable("collegeId") BigInteger collegeId,
                                                             @PathVariable("registrarId") BigInteger registrarId,
                                                             @RequestBody CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        return collegeService.updateRegisterRegistrar(collegeId, registrarId, collegeRegistrarCreationRequestTo);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_DEAN, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeDeanProfileTo registerDean(@PathVariable("collegeId") BigInteger collegeId, @RequestBody CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        return collegeService.registerDean(collegeId, collegeDeanCreationRequestTo);
    }

    /**
     * Updates and registers a dean for a college.
     *
     * @param collegeId                    the ID of the college to update the dean for.
     * @param deanId                       the ID of the dean to update.
     * @param collegeDeanCreationRequestTo the request body containing the new dean's information.
     * @return a {@link CollegeDeanProfileTo} representing the updated dean profile.
     * @throws NmrException if the college or dean IDs are invalid, or if there is an error registering the dean.
     */
    @PutMapping(path = "/college/{collegeId}/dean/{deanId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeDeanProfileTo updateRegisterDean(@PathVariable("collegeId") BigInteger collegeId,
                                                   @PathVariable("deanId") BigInteger deanId,
                                                   @RequestBody CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        return collegeService.updateRegisterDean(collegeId, deanId, collegeDeanCreationRequestTo);
    }

    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_ADMIN, RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo retrieveCollegeProfile(@PathVariable(name = "collegeId") BigInteger collegeId) throws NmrException {
        return collegeService.retrieveCollegeProfile(collegeId);
    }

    /**
     * Retrieves the profile of a college registrar with the given ID for the specified college ID.
     * This endpoint requires the caller to have the role "COLLEGE_REGISTRAR" and provide a valid
     * bearer token for authentication.
     *
     * @param registrarId the ID of the registrar to retrieve
     * @param collegeId   the ID of the college for which to retrieve the registrar's profile
     * @return the {@link CollegeRegistrarProfileTo} object containing the registrar's profile information
     * @throws NmrException if there is an error retrieving the registrar's profile
     */
    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_REGISTRAR_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_REGISTRAR})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(@PathVariable(name = "registrarId") BigInteger registrarId,
                                                              @PathVariable(name = "collegeId") BigInteger collegeId) throws NmrException {
        return collegeService.retrieveRegistrarProfile(registrarId, collegeId);
    }

    /**
     * Retrieves the College Dean's profile based on the collegeId and deanId
     *
     * @param collegeId - The Id of the college to which the Dean belongs to
     * @param deanId    - The Id of the Dean to retrieve profile of
     * @return CollegeDeanProfileTo - The profile of the requested Dean
     * @throws NmrException - If the Dean profile cannot be retrieved due to any reason
     */
    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_DEAN_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_DEAN})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeDeanProfileTo retrieveDeanProfile(@PathVariable(name = "collegeId") BigInteger collegeId,
                                                    @PathVariable(name = "deanId") BigInteger deanId) throws NmrException {
        return collegeService.retrieveDeanProfile(collegeId, deanId);
    }

    /**
     * Endpoint for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed in a page
     * @param search       - Gives the search criteria
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @param collegeId    - Search by collegeId
     * @param collegeName  - Search by collegeName
     * @param councilName  - Search by councilName
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval.
     */
    @GetMapping(path = NMRConstants.PATH_COLLEGE_APPLICATIONS)
    public CollegeRegistrationResponseTO getCollegeRegistrationDetails(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo, @RequestParam(required = false, value = "offset", defaultValue = "2") String offset, @RequestParam(required = false, value = "search") String search, @RequestParam(required = false, value = "id") String collegeId, @RequestParam(required = false, value = "name") String collegeName, @RequestParam(required = false, value = "council") String councilName, @RequestParam(required = false, value = "sortBy") String sortBy, @RequestParam(required = false, value = "sortType") String sortType) {
        return collegeService.getCollegeRegistrationDetails(pageNo, offset, search, collegeId, collegeName, councilName, sortBy, sortType);
    @GetMapping(path = NMRConstants.PATH_COLLEGE_REGISTRATION)
    public CollegeRegistrationResponseTO getCollegeRegistrationDetails(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo,
                                                                       @RequestParam(required = false, value = "limit", defaultValue = "2") String limit,
                                                                       @RequestParam(required = false, value = "filterCriteria") String filterCriteria,
                                                                       @RequestParam(required = false, value = "filterValue") String filterValue,
                                                                       @RequestParam(required = false, value = "sort") String columnToSort,
                                                                       @RequestParam(required = false, value = "sortingOrder") String sortOrder) {
        return collegeService.getCollegeRegistrationDetails(pageNo, limit, filterCriteria, filterValue, columnToSort, sortOrder);
    }
}
