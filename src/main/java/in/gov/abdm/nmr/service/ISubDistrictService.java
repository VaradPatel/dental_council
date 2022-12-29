package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.SubDistrictTO;

import java.math.BigInteger;
import java.util.List;

public interface ISubDistrictService {

    List<SubDistrictTO> getSubDistrictData(BigInteger districtId);
}
