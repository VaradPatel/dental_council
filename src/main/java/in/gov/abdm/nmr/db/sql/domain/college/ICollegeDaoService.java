package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;

public interface ICollegeDaoService {

    List<CollegeTO> getCollegeData(BigInteger universityId);
    College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update);
    College findById(BigInteger collegeId);
    College findByUserDetail(BigInteger userDetailId);
}
