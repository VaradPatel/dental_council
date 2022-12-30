package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.IMasterDataService;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.service.ICollegeDaoService;
import in.gov.abdm.nmr.service.ICourseService;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import in.gov.abdm.nmr.service.IRegistrationRenewationTypeService;

@Service
public class MasterDataServiceImpl implements IMasterDataService {

	private StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;

	private CountryServiceImpl countryService;

	private StateServiceImpl stateService;

	private DistrictServiceImpl districtService;
	
	private SubDistrictServiceImpl subDistrictService;
	
	private VillagesServiceImpl villagesServiceImpl;
	
	private BroadSpecialityServiceImpl broadSpecialityServiceImpl;
	
	private UniversityServiceImpl universityService;
	
	private LanguageServiceImpl languageService;

	private ICollegeDaoService collegeService;

	private IMasterDataMapper masterDataMapper;
	
	private ICourseService courseService;
	
	private IRegistrationRenewationTypeService registrationRenewationTypeService;

	private IFacilityTypeService facilityTypeService;

	public MasterDataServiceImpl(StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService, IMasterDataMapper masterDataMapper,
								 CountryServiceImpl countryService, StateServiceImpl stateService, DistrictServiceImpl districtService, SubDistrictServiceImpl subDistrictService,
								 VillagesServiceImpl villagesServiceImpl, BroadSpecialityServiceImpl broadSpecialityServiceImpl, UniversityServiceImpl universityService, ICollegeDaoService collegeService,
								 LanguageServiceImpl languageService, ICourseService courseService, IRegistrationRenewationTypeService registrationRenewationTypeService,
								 IFacilityTypeService facilityTypeService) {
		super();
		this.stateMedicalCouncilService = stateMedicalCouncilService;
		this.masterDataMapper = masterDataMapper;
		this.countryService = countryService;
		this.stateService = stateService;
		this.districtService = districtService;
		this.subDistrictService = subDistrictService;
		this.villagesServiceImpl = villagesServiceImpl;
		this.broadSpecialityServiceImpl = broadSpecialityServiceImpl;
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
		return masterDataMapper.specialitiesToMasterDataTOs(broadSpecialityServiceImpl.getSpecialityData());
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
        return masterDataMapper.citiesToMasterDataTOs(villagesServiceImpl.getCityData(subDistrictId));
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
