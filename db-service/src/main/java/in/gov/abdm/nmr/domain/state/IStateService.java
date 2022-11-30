package in.gov.abdm.nmr.domain.state;

import java.math.BigInteger;
import java.util.List;

public interface IStateService {

	List<StateTO> getStateData(BigInteger countryId);
}
