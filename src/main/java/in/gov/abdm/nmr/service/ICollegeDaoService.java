package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.dto.CollegeTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeDaoService {

    List<CollegeTO> getCollegeData(BigInteger universityId);
    College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException;
    College findById(BigInteger collegeId);
    College findByUserDetail(BigInteger userDetailId);
}
