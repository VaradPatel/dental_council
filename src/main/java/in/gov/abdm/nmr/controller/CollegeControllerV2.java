package in.gov.abdm.nmr.controller;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.CollegeDesignationTO;
import in.gov.abdm.nmr.dto.CollegeMasterTOV2;
import in.gov.abdm.nmr.dto.CollegeProfileTOV2;
import in.gov.abdm.nmr.service.ICollegeServiceV2;

@RestController
public class CollegeControllerV2 {

    private ICollegeServiceV2 collegeServiceV2;

    public CollegeControllerV2(ICollegeServiceV2 collegeServiceV2) {
        this.collegeServiceV2 = collegeServiceV2;
    }

    @GetMapping(path = "/colleges", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterTOV2> getAllColleges() {
        return collegeServiceV2.getAllColleges();
    }

    @GetMapping(path = "/colleges/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeMasterTOV2 getCollege(@PathVariable BigInteger id) {
        return collegeServiceV2.getCollege(id);
    }

    @PostMapping(path = "/colleges", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeMasterTOV2 createCollege(@Valid @RequestBody CollegeMasterTOV2 collegeMasterTOV2) {
        return collegeServiceV2.createOrUpdateCollege(collegeMasterTOV2);
    }

    @PutMapping(path = "/colleges/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeMasterTOV2 updateCollege(@NotNull @PathVariable BigInteger id, @Valid @RequestBody CollegeMasterTOV2 collegeMasterTOV2) {
        collegeMasterTOV2.setId(id);
        return collegeServiceV2.createOrUpdateCollege(collegeMasterTOV2);
    }

    @GetMapping(path = "/colleges/verifiers/designation", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeDesignationTO> getAllCollegeVerifiersDesignation() {
        return collegeServiceV2.getAllCollegeVerifiersDesignation();
    }

    @PostMapping(path = "/colleges/{collegeId}/verifiers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTOV2 createCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @Valid @RequestBody CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException {
        collegeProfileTOV2.setCollegeId(collegeId);
        return collegeServiceV2.createOrUpdateCollegeVerifier(collegeProfileTOV2);
    }

    @PutMapping(path = "/colleges/{collegeId}/verifiers/{verifierId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTOV2 updateCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @NotNull @PathVariable BigInteger verifierId, @Valid @RequestBody CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException {
        collegeProfileTOV2.setCollegeId(collegeId);
        collegeProfileTOV2.setId(verifierId);
        return collegeServiceV2.createOrUpdateCollegeVerifier(collegeProfileTOV2);
    }

    @GetMapping(path = "/colleges/{collegeId}/verifiers/{verifierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTOV2 getCollegeVerifier(@NotNull @PathVariable BigInteger collegeId, @NotNull @PathVariable BigInteger verifierId) {
        return collegeServiceV2.getCollegeVerifier(collegeId, verifierId);
    }
}
