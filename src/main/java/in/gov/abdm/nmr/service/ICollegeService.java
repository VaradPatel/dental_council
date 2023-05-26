package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import in.gov.abdm.nmr.dto.CollegeMasterDataTO;
import in.gov.abdm.nmr.dto.CollegeResponseTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.*;

public interface ICollegeService {

    List<CollegeMasterDataTO> getAllColleges() throws NmrException;

    CollegeResponseTo getCollege(BigInteger id) throws NmrException, InvalidIdException, NotFoundException;

    CollegeResponseTo createOrUpdateCollege(CollegeResponseTo collegeResponseTo) throws NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException, NotFoundException;

    CollegeProfileTo createOrUpdateCollegeVerifier(CollegeProfileTo collegeProfileTo) throws GeneralSecurityException, NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException;

    List<CollegeMasterDataTO> getAllCollegeVerifiersDesignation() throws NmrException;

    CollegeProfileTo getCollegeVerifier(BigInteger collegeId, BigInteger verifierId) throws NmrException, InvalidIdException;

    User getLoggedInUser();
}
