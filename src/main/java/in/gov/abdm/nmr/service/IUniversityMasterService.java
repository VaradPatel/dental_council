package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.UniversityMasterTo;

import java.math.BigInteger;
import java.util.List;

public interface IUniversityMasterService {
    List<UniversityMasterTo> getUniversitiesByCollegeId(BigInteger collegeId);
}
