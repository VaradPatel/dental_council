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

    @PostMapping(path = "college", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo registerCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException, WorkFlowException {
        return collegeService.registerCollege(collegeRegistrationRequestTo, false);
    }

    @PutMapping(path = ProtectedPaths.PATH_UPDATE_COLLEGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo updateCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException, WorkFlowException {
        return collegeService.registerCollege(collegeRegistrationRequestTo, true);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_REGISTRAR, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeRegistrarProfileTo registerRegistrar(@RequestBody CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        return collegeService.registerRegistrar(collegeRegistrarCreationRequestTo);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_DEAN, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeDeanProfileTo registerDean(@RequestBody CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        return collegeService.registerDean(collegeDeanCreationRequestTo);
    }

    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_ADMIN, RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo retrieveCollegeProfile(@PathVariable(name = "id") BigInteger collegeId) throws NmrException {
        return collegeService.retrieveCollegeProfile(collegeId);
    }

    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_REGISTRAR_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_REGISTRAR})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(@PathVariable(name = "id") BigInteger registrarId) throws NmrException {
        return collegeService.retrieveRegistrarProfile(registrarId);
    }

    @GetMapping(path = ProtectedPaths.PATH_COLLEGE_DEAN_PROFILE)
    //---
    @RolesAllowed({RoleConstants.COLLEGE_DEAN})
    @SecurityRequirement(name = "bearerAuth")
    public CollegeDeanProfileTo retrieveDeanProfile(@PathVariable(name = "id") BigInteger id) throws NmrException {
        return collegeService.retrieveDeanProfile(id);
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
    @GetMapping(path = NMRConstants.PATH_COLLEGE_REGISTRATION)
    public CollegeRegistrationResponseTO getCollegeRegistrationDetails(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo, @RequestParam(required = false, value = "offset", defaultValue = "2") String offset, @RequestParam(required = false, value = "search") String search, @RequestParam(required = false, value = "id") String collegeId, @RequestParam(required = false, value = "name") String collegeName, @RequestParam(required = false, value = "council") String councilName, @RequestParam(required = false, value = "sortBy") String sortBy, @RequestParam(required = false, value = "sortType") String sortType) {
        return collegeService.getCollegeRegistrationDetails(pageNo, offset, search, collegeId, collegeName, councilName, sortBy, sortType);
    }
}
