package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;

public interface IMasterDataService {

    List<MasterDataTO> smcs();
    
    List<MasterDataTO> specialities();
    
    List<MasterDataTO> countries();
    
    List<MasterDataTO> states(BigInteger countryId);

    List<MasterDataTO> districts(BigInteger stateId);
    
    List<MasterDataTO> subDistricts(BigInteger districtId);

    List<MasterDataTO> cities(BigInteger subDistrictId);
    
    List<MasterDataTO> universities();
    
    List<MasterDataTO> colleges(BigInteger universityId);
    
    List<MasterDataTO> languages();
    
    List<MasterDataTO> courses();

    List<MasterDataTO> registrationRenewationType();
    
    List<MasterDataTO> facilityType();
}
