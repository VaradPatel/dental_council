package in.gov.abdm.nmr.api.controller.md;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.codec.language.bm.Languages;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.md.to.IMasterDataMapper;
import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;
import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpecialityService;
import in.gov.abdm.nmr.db.sql.domain.city.CityService;
import in.gov.abdm.nmr.db.sql.domain.college.CollegeService;
import in.gov.abdm.nmr.db.sql.domain.country.CountryService;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictService;
import in.gov.abdm.nmr.db.sql.domain.language.LanguageService;
import in.gov.abdm.nmr.db.sql.domain.state.StateService;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncilService;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrictService;
import in.gov.abdm.nmr.db.sql.domain.university.UniversityService;

@Service
public class MasterDataService implements IMasterDataService {

	private StateMedicalCouncilService stateMedicalCouncilService;

	private CountryService countryService;

	private StateService stateService;

	private DistrictService districtService;
	
	private SubDistrictService subDistrictService;
	
	private CityService cityService;
	
	private BroadSpecialityService broadSpecialityService;
	
	private UniversityService universityService;
	
	private LanguageService languageService;

	private CollegeService collegeService;

	private IMasterDataMapper masterDataMapper;

	public MasterDataService(StateMedicalCouncilService stateMedicalCouncilService, IMasterDataMapper masterDataMapper,
			CountryService countryService, StateService stateService, DistrictService districtService, SubDistrictService subDistrictService,
			CityService cityService, BroadSpecialityService broadSpecialityService, UniversityService universityService, CollegeService collegeService,
			LanguageService languageService) {
		super();
		this.stateMedicalCouncilService = stateMedicalCouncilService;
		this.masterDataMapper = masterDataMapper;
		this.countryService = countryService;
		this.stateService = stateService;
		this.districtService = districtService;
		this.subDistrictService = subDistrictService;
		this.cityService = cityService;
		this.broadSpecialityService = broadSpecialityService;
		this.universityService = universityService;
		this.collegeService = collegeService;
		this.languageService = languageService;
	}

	@Override
	public List<MasterDataTO> smcs() {
		return masterDataMapper.stateMedicalCouncilsToMasterDataTOs(stateMedicalCouncilService.smcs());
	}
	
	@Override
	public List<MasterDataTO> specialities() {
		return masterDataMapper.specialitiesToMasterDataTOs(broadSpecialityService.getSpecialityData());
	}

	@Override
	public List<MasterDataTO> countries() {
		return masterDataMapper.countriesToMasterDataTOs(countryService.getCountryData());
	}

	@Override
	public List<MasterDataTO> states(BigInteger countryId) {
		return masterDataMapper.statesToMasterDataTOs(stateService.getStateData(countryId));
	}

	@Override
	public List<MasterDataTO> districts(BigInteger stateId) {
		return masterDataMapper.districtsToMasterDataTOs(districtService.getDistrictData(stateId));
	}
	
	@Override
	public List<MasterDataTO> subDistricts(BigInteger districtId) {
		return masterDataMapper.subDistrictsToMasterDataTOs(subDistrictService.getSubDistrictData(districtId));
	}

    @Override
    public List<MasterDataTO> cities(BigInteger subDistrictId) {
        return masterDataMapper.citiesToMasterDataTOs(cityService.getCityData(subDistrictId));
    }
    
	@Override
	public List<MasterDataTO> universities() {
		return masterDataMapper.universitiesToMasterDataTOs(universityService.getUniversityData());
	}
	
	@Override
	public List<MasterDataTO> colleges(BigInteger universityId) {
		return masterDataMapper.collegesToMasterDataTOs(collegeService.getCollegeData(universityId));
	}
	
	@Override
	public List<MasterDataTO> languages() {
		return masterDataMapper.languagesToMasterDataTOs(languageService.getLanguageData());
	}
}
