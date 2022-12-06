package in.gov.abdm.nmr.db.sql.domain.sub_district;

import java.util.List;

public interface ISubDistrictService {

    List<SubDistrictTO> getSubDistrictData(Long districtId);
}
