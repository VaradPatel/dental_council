package in.gov.abdm.nmr.api.controller.md;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.md.to.IMasterDataMapper;
import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;
import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpecialityService;
import in.gov.abdm.nmr.db.sql.domain.college.ICollegeDaoService;
import in.gov.abdm.nmr.db.sql.domain.country.CountryService;
import in.gov.abdm.nmr.db.sql.domain.course.ICourseService;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictService;
import in.gov.abdm.nmr.db.sql.domain.facility_type.IFacilityTypeService;
import in.gov.abdm.nmr.db.sql.domain.language.LanguageService;
import in.gov.abdm.nmr.db.sql.domain.registration_renewation_type.IRegistrationRenewationTypeService;
import in.gov.abdm.nmr.db.sql.domain.state.StateService;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncilDaoService;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrictService;
import in.gov.abdm.nmr.db.sql.domain.university.UniversityService;
import in.gov.abdm.nmr.db.sql.domain.villages.VillagesService;

@Service
public class MasterDataService implements IMasterDataService {

	private StateMedicalCouncilDaoService stateMedicalCouncilService;

	private CountryService countryService;

	private StateService stateService;

	private DistrictService districtService;
	
	private SubDistrictService subDistrictService;
	
	private VillagesService villagesService;
	
	private BroadSpecialityService broadSpecialityService;
	
	private UniversityService universityService;
	
	private LanguageService languageService;

	private ICollegeDaoService collegeService;

	private IMasterDataMapper masterDataMapper;
	
	private ICourseService courseService;
	
	private IRegistrationRenewationTypeService registrationRenewationTypeService;

	private IFacilityTypeService facilityTypeService;

	public MasterDataService(StateMedicalCouncilDaoService stateMedicalCouncilService, IMasterDataMapper masterDataMapper,
			CountryService countryService, StateService stateService, DistrictService districtService, SubDistrictService subDistrictService,
			VillagesService villagesService, BroadSpecialityService broadSpecialityService, UniversityService universityService, ICollegeDaoService collegeService,
			LanguageService languageService, ICourseService courseService, IRegistrationRenewationTypeService registrationRenewationTypeService,
			IFacilityTypeService facilityTypeService) {
		super();
		this.stateMedicalCouncilService = stateMedicalCouncilService;
		this.masterDataMapper = masterDataMapper;
		this.countryService = countryService;
		this.stateService = stateService;
		this.districtService = districtService;
		this.subDistrictService = subDistrictService;
		this.villagesService = villagesService;
		this.broadSpecialityService = broadSpecialityService;
		this.universityService = universityService;
		this.collegeService = collegeService;
		this.languageService = languageService;
		this.courseService = courseService;
		this.registrationRenewationTypeService = registrationRenewationTypeService;
		this.facilityTypeService = facilityTypeService;
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
        return masterDataMapper.citiesToMasterDataTOs(villagesService.getCityData(subDistrictId));
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
	
	@Override
	public List<MasterDataTO> courses() {
		return masterDataMapper.coursesToMasterDataTOs(courseService.getCourseData());
	}
	
	@Override
	public List<MasterDataTO> registrationRenewationType() {
		return masterDataMapper.registrationRenewationTypeDataTOs(registrationRenewationTypeService.getRegistrationRenewationType());
	}

	@Override
	public List<MasterDataTO> facilityType() {
		return masterDataMapper.facilityTypeDataTOs(facilityTypeService.getFacilityType());
	}
	
	
}
