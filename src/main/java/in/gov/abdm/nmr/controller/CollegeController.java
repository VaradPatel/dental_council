package in.gov.abdm.nmr.controller;

import java.math.BigInteger;

import in.gov.abdm.nmr.service.ICollegeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeDeanProfileTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.exception.NmrException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class CollegeController {

    private ICollegeService collegeService;

    public CollegeController(ICollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping(path = "college", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo registerCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException {
        return collegeService.registerCollege(collegeRegistrationRequestTo, false);
    }

    @PutMapping(path = ProtectedPaths.PATH_UPDATE_COLLEGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo updateCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo) throws NmrException {
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

    @GetMapping(path = "college/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public CollegeProfileTo retrieveCollegeProfile(@PathVariable(name = "id") BigInteger collegeId) throws NmrException {
        return collegeService.retrieveCollegeProfile(collegeId);
    }

    @GetMapping(path = "college/registrar/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(@PathVariable(name = "id") BigInteger registrarId) throws NmrException {
        return collegeService.retrieveRegistrarProfile(registrarId);
    }

    @GetMapping(path = "college/dean/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public CollegeDeanProfileTo retrieveDeanProfile(@PathVariable(name = "id") BigInteger id) throws NmrException {
        return collegeService.retrieveDeanProfile(id);
    }
}