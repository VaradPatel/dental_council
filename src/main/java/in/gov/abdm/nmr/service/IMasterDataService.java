package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;

import java.math.BigInteger;
import java.util.List;

public interface IMasterDataService {

    List<MasterDataTO> smcs();

    List<MasterDataTO> specialities();

    List<MasterDataTO> countries();

    List<MasterDataTO> states(BigInteger countryId);

    List<MasterDataTO> districts(BigInteger stateId);

    List<MasterDataTO> subDistricts(BigInteger districtId);

    List<MasterDataTO> cities(BigInteger subDistrictId);

    List<MasterDataTO> languages();

    List<MasterDataTO> courses();

    List<MasterDataTO> registrationRenewationType();

    List<MasterDataTO> facilityType();

    List<CollegeMasterResponseTo> getCollegesByState(BigInteger stateId);

    List<UniversityMasterResponseTo> getUniversitiesByCollege(BigInteger collegeId);
}
