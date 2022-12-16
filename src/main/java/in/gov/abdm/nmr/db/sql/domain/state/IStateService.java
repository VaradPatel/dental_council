package in.gov.abdm.nmr.db.sql.domain.state;

import java.math.BigInteger;
import java.util.List;

public interface IStateService {

    List<StateTO> getStateData(BigInteger countryId);
}
