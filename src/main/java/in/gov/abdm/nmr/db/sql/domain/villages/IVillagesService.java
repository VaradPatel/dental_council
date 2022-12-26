package in.gov.abdm.nmr.db.sql.domain.villages;

import java.math.BigInteger;
import java.util.List;

public interface IVillagesService {

    List<VillagesTO> getCityData(BigInteger subDistrictId);
}
