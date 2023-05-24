package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.QuerySuggestionsTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.mapper.CourseMasterToMapper;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
import in.gov.abdm.nmr.mapper.QuerySuggestionsDtoMapper;
import in.gov.abdm.nmr.repository.QuerySuggestionsRepository;
import in.gov.abdm.nmr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MasterDataServiceImpl implements IMasterDataService {
    @Autowired
    private StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;
    @Autowired
    private CountryServiceImpl countryService;
    @Autowired
    private StateServiceImpl stateService;
    @Autowired
    private DistrictServiceImpl districtService;
    @Autowired
    private SubDistrictServiceImpl subDistrictService;
    @Autowired
    private VillagesServiceImpl villagesServiceImpl;
    @Autowired
    private BroadSpecialityServiceImpl broadSpecialityServiceImpl;
    @Autowired
    private UniversityServiceImpl universityService;
    @Autowired
    private LanguageServiceImpl languageService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IRegistrationRenewationTypeService registrationRenewationTypeService;
    @Autowired
    private IFacilityTypeService facilityTypeService;
    @Autowired
    private ICollegeMasterService collegeMasterService;
    @Autowired
    private IUniversityMasterService universityMasterService;

    @Autowired
    private QuerySuggestionsRepository querySuggestionsRepository;

    @Override
    public List<MasterDataTO> smcs() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.stateMedicalCouncilsToMasterDataTOs(stateMedicalCouncilService.getAllStateMedicalCouncil());
    }

    @Override
    public List<MasterDataTO> specialities() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.specialitiesToMasterDataTOs(broadSpecialityServiceImpl.getSpecialityData());
    }

    @Override
    public List<MasterDataTO> countries() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.countriesToMasterDataTOs(countryService.getCountryData());
    }

    @Override
    public List<MasterDataTO> states(BigInteger countryId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.statesToMasterDataTOs(stateService.getStateData(countryId));
    }

    @Override
    public List<MasterDataTO> districts(BigInteger stateId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.districtsToMasterDataTOs(districtService.getDistrictData(stateId));
    }

    @Override
    public List<MasterDataTO> subDistricts(BigInteger districtId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.subDistrictsToMasterDataTOs(subDistrictService.getSubDistrictData(districtId));
    }

    @Override
    public List<MasterDataTO> cities(BigInteger subDistrictId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.citiesToMasterDataTOs(villagesServiceImpl.getCityData(subDistrictId));
    }

    @Override
    public List<MasterDataTO> languages() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.languagesToMasterDataTOs(languageService.getLanguageData());
    }

    @Override
    public List<MasterDataTO> courses() {

        return CourseMasterToMapper.courseToListToCourseMasterToList(courseService.getCourseData());
    }

    @Override
    public List<MasterDataTO> registrationRenewationType() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.registrationRenewationTypeDataTOs(registrationRenewationTypeService.getRegistrationRenewationType());
    }

    @Override
    public List<MasterDataTO> facilityType() {
        return IMasterDataMapper.MASTER_DATA_MAPPER.facilityTypeDataTOs(facilityTypeService.getFacilityType());
    }

    @Override
    public List<CollegeMasterResponseTo> getCollegesByState(BigInteger stateId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.collegeMasterResponseDataTo(collegeMasterService.getCollegesByStateId(stateId));
    }

    @Override
    public List<UniversityMasterResponseTo> getUniversitiesByCollege(BigInteger collegeId) {
        return IMasterDataMapper.MASTER_DATA_MAPPER.universityMasterResponseDataTo(universityMasterService.getUniversitiesByCollegeId(collegeId));
    }

    /**
     * get query suggestions
     * @return List of queries object
     */
    @Override
    public List<QuerySuggestionsTo> getQuerySuggestions() {
        return QuerySuggestionsDtoMapper.QUERY_SUGGESTIONS_DTO_MAPPER.querySuggestionToDto(querySuggestionsRepository.findAll());
    }

}
