package in.gov.abdm.nmr.domain.city;

import java.math.BigInteger;
import java.util.List;

public interface ICityService {

	List<CityTO> getCityData(BigInteger subDistrictId);
}
