package in.gov.abdm.nmr.db.sql.domain.city;

import java.math.BigInteger;
import java.util.List;

public interface ICityService {

    List<CityTO> getCityData(Long subDistrictId);
}
