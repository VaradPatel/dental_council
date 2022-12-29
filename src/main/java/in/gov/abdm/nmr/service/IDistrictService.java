package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.DistrictTO;

import java.math.BigInteger;
import java.util.List;

public interface IDistrictService {

    List<DistrictTO> getDistrictData(BigInteger stateId);

}
