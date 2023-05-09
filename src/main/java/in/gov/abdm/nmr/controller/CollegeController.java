package in.gov.abdm.nmr.controller;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import in.gov.abdm.nmr.exception.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.CollegeMasterDataTO;
import in.gov.abdm.nmr.dto.CollegeResponseTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.ICollegeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class CollegeController {

    public static final String COLLEGES_COLLEGE_ID_VERIFIERS_VERIFIER_ID = "/colleges/{collegeId}/verifiers/{verifierId}";

    public static final String COLLEGES_COLLEGE_ID_VERIFIERS = "/colleges/{collegeId}/verifiers";

    public static final String COLLEGES_VERIFIERS_DESIGNATION = "/colleges/verifiers/designations";

    public static final String COLLEGES = "/colleges";

    public static final String COLLEGE_ID = "/colleges/{id}";

    private ICollegeService collegeServiceV2;

    public CollegeController(ICollegeService collegeServiceV2) {
        this.collegeServiceV2 = collegeServiceV2;
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = COLLEGES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterDataTO> getAllColleges() throws NmrException {
        return collegeServiceV2.getAllColleges();
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN, RoleConstants.COLLEGE_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = COLLEGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeResponseTo getCollege(@PathVariable BigInteger id) throws NmrException, InvalidIdException, NotFoundException {
        return collegeServiceV2.getCollege(id);
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = COLLEGES, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeResponseTo createCollege(@Valid @RequestBody CollegeResponseTo collegeResponseTo) throws NmrException, InvalidRequestException, ResourceExistsException, InvalidIdException, NotFoundException {
        return collegeServiceV2.createOrUpdateCollege(collegeResponseTo);
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN, RoleConstants.COLLEGE_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(path = COLLEGE_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeResponseTo updateCollege(@NotNull @PathVariable BigInteger id, @Valid @RequestBody CollegeResponseTo collegeResponseTo) throws NmrException, InvalidRequestException, ResourceExistsException, InvalidIdException, NotFoundException {
        collegeResponseTo.setId(id);
        return collegeServiceV2.createOrUpdateCollege(collegeResponseTo);
    }

    @RolesAllowed({RoleConstants.COLLEGE_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = COLLEGES_VERIFIERS_DESIGNATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterDataTO> getAllCollegeVerifiersDesignation() throws NmrException {
        return collegeServiceV2.getAllCollegeVerifiersDesignation();
    }

    @RolesAllowed({RoleConstants.COLLEGE_ADMIN})
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = COLLEGES_COLLEGE_ID_VERIFIERS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo createCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @Valid @RequestBody CollegeProfileTo collegeProfileTo) throws GeneralSecurityException, NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException {
        collegeProfileTo.setCollegeId(collegeId);
        return collegeServiceV2.createOrUpdateCollegeVerifier(collegeProfileTo);
    }

    @RolesAllowed({RoleConstants.COLLEGE_DEAN, RoleConstants.COLLEGE_REGISTRAR})
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(path = COLLEGES_COLLEGE_ID_VERIFIERS_VERIFIER_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo updateCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @NotNull @PathVariable BigInteger verifierId, @Valid @RequestBody CollegeProfileTo collegeProfileTo) throws GeneralSecurityException, NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException {
        collegeProfileTo.setCollegeId(collegeId);
        collegeProfileTo.setId(verifierId);
        return collegeServiceV2.createOrUpdateCollegeVerifier(collegeProfileTo);
    }

    @RolesAllowed({RoleConstants.COLLEGE_DEAN, RoleConstants.COLLEGE_REGISTRAR})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = COLLEGES_COLLEGE_ID_VERIFIERS_VERIFIER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo getCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @NotNull @PathVariable BigInteger verifierId) throws NmrException, InvalidIdException {
        return collegeServiceV2.getCollegeVerifier(collegeId, verifierId);
    }
}
