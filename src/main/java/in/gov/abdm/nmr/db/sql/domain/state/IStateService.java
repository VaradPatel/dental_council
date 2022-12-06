package in.gov.abdm.nmr.db.sql.domain.state;

import java.util.List;

public interface IStateService {

    List<StateTO> getStateData(Long countryId);
}
