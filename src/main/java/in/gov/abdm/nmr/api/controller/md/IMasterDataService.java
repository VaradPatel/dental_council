package in.gov.abdm.nmr.api.controller.md;

import java.util.List;

import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;

public interface IMasterDataService {

    List<MasterDataTO> smcs();
    
    List<MasterDataTO> specialities();
    
    List<MasterDataTO> countries();
    
    List<MasterDataTO> states(Long countryId);

    List<MasterDataTO> districts(Long stateId);
    
    List<MasterDataTO> subDistricts(Long districtId);

    List<MasterDataTO> cities(Long subDistrictId);
    
    List<MasterDataTO> universities();
    
    List<MasterDataTO> colleges(Long universityId);
    
    List<MasterDataTO> languages();

}
