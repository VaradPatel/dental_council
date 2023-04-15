package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.mapper.CourseMasterToMapper;
import in.gov.abdm.nmr.mapper.IMasterDataMapper;
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
    private IMasterDataMapper masterDataMapper;
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


    @Override
    public List<MasterDataTO> smcs() {
        return masterDataMapper.stateMedicalCouncilsToMasterDataTOs(stateMedicalCouncilService.getAllStateMedicalCouncil());
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
    public List<MasterDataTO> languages() {
        return masterDataMapper.languagesToMasterDataTOs(languageService.getLanguageData());
    }

    @Override
    public List<MasterDataTO> courses() {

        return CourseMasterToMapper.courseToListToCourseMasterToList(courseService.getCourseData());
    }

    @Override
    public List<MasterDataTO> registrationRenewationType() {
        return masterDataMapper.registrationRenewationTypeDataTOs(registrationRenewationTypeService.getRegistrationRenewationType());
    }

    @Override
    public List<MasterDataTO> facilityType() {
        return masterDataMapper.facilityTypeDataTOs(facilityTypeService.getFacilityType());
    }

    @Override
    public List<CollegeMasterResponseTo> getCollegesByState(BigInteger stateId) {
        return masterDataMapper.collegeMasterResponseDataTo(collegeMasterService.getCollegesByStateId(stateId));
    }

    @Override
    public List<UniversityMasterResponseTo> getUniversitiesByCollege(BigInteger collegeId) {
        return masterDataMapper.universityMasterResponseDataTo(universityMasterService.getUniversitiesByCollegeId(collegeId));
    }

    @Override
    public List<CollegeMasterResponseTo> getCollegesByUniversity(BigInteger universityId) {
        return masterDataMapper.collegeMasterResponseDataTo(collegeMasterService.getCollegesByUniversity(universityId));
    }

    @Override
    public List<UniversityMasterResponseTo> getUniversitiesByState(BigInteger stateId) {
        return masterDataMapper.universityMasterResponseDataTo(universityMasterService.getUniversitiesByState(stateId));

    }
}
