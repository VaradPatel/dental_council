package in.gov.abdm.nmr.db.sql.domain.college;

import java.util.List;

public interface ICollegeService {

    List<CollegeTO> getCollegeData(Long collegeId);
}
