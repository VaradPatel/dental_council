package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.StateTO;

import java.math.BigInteger;
import java.util.List;

public interface IStateService {

    List<StateTO> getStateData(BigInteger countryId);
}
