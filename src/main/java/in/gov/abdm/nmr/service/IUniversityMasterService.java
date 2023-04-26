package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.UniversityMasterTo;
import in.gov.abdm.nmr.entity.UniversityMaster;

import java.math.BigInteger;
import java.util.List;

public interface IUniversityMasterService {
    
    List<UniversityMasterTo> getUniversitiesByCollegeId(BigInteger collegeId);

    UniversityMaster findById(BigInteger id);
    
    UniversityMaster save(UniversityMaster universityMaster);
}
