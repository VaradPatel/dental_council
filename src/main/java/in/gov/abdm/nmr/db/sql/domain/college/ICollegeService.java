package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;
import java.util.List;

public interface ICollegeService {

    List<CollegeTO> getCollegeData(BigInteger collegeId);
}
