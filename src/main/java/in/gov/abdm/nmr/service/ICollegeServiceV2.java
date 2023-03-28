package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import in.gov.abdm.nmr.dto.CollegeMasterDataTO;
import in.gov.abdm.nmr.dto.CollegeMasterTOV2;
import in.gov.abdm.nmr.dto.CollegeProfileTOV2;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeServiceV2 {

    List<CollegeMasterDataTO> getAllColleges() throws NmrException;

    CollegeMasterTOV2 getCollege(BigInteger id) throws NmrException;

    CollegeMasterTOV2 createOrUpdateCollege(CollegeMasterTOV2 collegeMasterTOV2) throws NmrException;

    CollegeProfileTOV2 createOrUpdateCollegeVerifier(CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException, NmrException;

    List<CollegeMasterDataTO> getAllCollegeVerifiersDesignation() throws NmrException;

    CollegeProfileTOV2 getCollegeVerifier(BigInteger collegeId, BigInteger verifierId) throws NmrException;
}
