package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.CollegeProfile;

public interface ICollegeProfileDaoService {
    
    CollegeProfile findById(BigInteger id);
    
    CollegeProfile save(CollegeProfile collegeProfile);
    
    CollegeProfile findAdminByCollegeId(BigInteger collegeId);

    CollegeProfile findByUserId(BigInteger userId);
}
