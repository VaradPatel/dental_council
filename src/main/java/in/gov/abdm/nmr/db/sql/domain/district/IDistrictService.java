package in.gov.abdm.nmr.db.sql.domain.district;

import java.math.BigInteger;
import java.util.List;

public interface IDistrictService {

    List<DistrictTO> getDistrictData(Long stateId);

}
