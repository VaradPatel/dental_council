package in.gov.abdm.nmr.db.sql.domain.sub_district;

import java.math.BigInteger;
import java.util.List;

public interface ISubDistrictService {

    List<SubDistrictTO> getSubDistrictData(BigInteger districtId);
}
