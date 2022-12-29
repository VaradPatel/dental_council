package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.VillagesTO;

import java.math.BigInteger;
import java.util.List;

public interface IVillagesService {

    List<VillagesTO> getCityData(BigInteger subDistrictId);
}
