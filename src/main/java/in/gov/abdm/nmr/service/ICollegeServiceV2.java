package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import in.gov.abdm.nmr.dto.CollegeDesignationTO;
import in.gov.abdm.nmr.dto.CollegeMasterTOV2;
import in.gov.abdm.nmr.dto.CollegeProfileTOV2;

public interface ICollegeServiceV2 {

    List<CollegeMasterTOV2> getAllColleges();

    CollegeMasterTOV2 getCollege(BigInteger id);

    CollegeMasterTOV2 createOrUpdateCollege(CollegeMasterTOV2 collegeMasterTOV2);

    CollegeProfileTOV2 createOrUpdateCollegeVerifier(CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException;

    List<CollegeDesignationTO> getAllCollegeVerifiersDesignation();

    CollegeProfileTOV2 getCollegeVerifier(BigInteger collegeId, BigInteger verifierId);
}
