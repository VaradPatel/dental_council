package in.gov.abdm.nmr.api.controller.md;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;

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

}
