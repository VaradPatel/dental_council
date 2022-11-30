package in.gov.abdm.nmr.domain.district;

import java.math.BigInteger;
import java.util.List;

public interface IDistrictService {

	List<DistrictTO> getDistrictData(BigInteger stateId);
}
