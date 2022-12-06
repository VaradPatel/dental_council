package in.gov.abdm.nmr.api.controller.md;

import java.util.List;

import org.apache.commons.codec.language.bm.Languages;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.md.to.IMasterDataMapper;
import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;
import in.gov.abdm.nmr.db.sql.domain.city.CityService;
import in.gov.abdm.nmr.db.sql.domain.college.CollegeService;
import in.gov.abdm.nmr.db.sql.domain.country.CountryService;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictService;
import in.gov.abdm.nmr.db.sql.domain.language.LanguageService;
import in.gov.abdm.nmr.db.sql.domain.speciality.SpecialityService;
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
	
	private SpecialityService specialityService;
	
	private UniversityService universityService;
	
	private LanguageService languageService;

	private CollegeService collegeService;

	private IMasterDataMapper masterDataMapper;

	public MasterDataService(StateMedicalCouncilService stateMedicalCouncilService, IMasterDataMapper masterDataMapper,
			CountryService countryService, StateService stateService, DistrictService districtService, SubDistrictService subDistrictService,
			CityService cityService, SpecialityService specialityService, UniversityService universityService, CollegeService collegeService,
			LanguageService languageService) {
		super();
		this.stateMedicalCouncilService = stateMedicalCouncilService;
		this.masterDataMapper = masterDataMapper;
		this.countryService = countryService;
		this.stateService = stateService;
		this.districtService = districtService;
		this.subDistrictService = subDistrictService;
		this.cityService = cityService;
		this.specialityService = specialityService;
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
		return masterDataMapper.specialitiesToMasterDataTOs(specialityService.getSpecialityData());
	}

	@Override
	public List<MasterDataTO> countries() {
		return masterDataMapper.countriesToMasterDataTOs(countryService.getCountryData());
	}

	@Override
	public List<MasterDataTO> states(Long countryId) {
		return masterDataMapper.statesToMasterDataTOs(stateService.getStateData(countryId));
	}

	@Override
	public List<MasterDataTO> districts(Long stateId) {
		return masterDataMapper.districtsToMasterDataTOs(districtService.getDistrictData(stateId));
	}
	
	@Override
	public List<MasterDataTO> subDistricts(Long districtId) {
		return masterDataMapper.subDistrictsToMasterDataTOs(subDistrictService.getSubDistrictData(districtId));
	}

    @Override
    public List<MasterDataTO> cities(Long subDistrictId) {
        return masterDataMapper.citiesToMasterDataTOs(cityService.getCityData(subDistrictId));
    }
    
	@Override
	public List<MasterDataTO> universities() {
		return masterDataMapper.universitiesToMasterDataTOs(universityService.getUniversityData());
	}
	
	@Override
	public List<MasterDataTO> colleges(Long universityId) {
		return masterDataMapper.collegesToMasterDataTOs(collegeService.getCollegeData(universityId));
	}
	
	@Override
	public List<MasterDataTO> languages() {
		return masterDataMapper.languagesToMasterDataTOs(languageService.getLanguageData());
	}
}
