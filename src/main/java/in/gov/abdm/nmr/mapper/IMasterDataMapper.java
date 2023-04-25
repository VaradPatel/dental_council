package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IMasterDataMapper {
    IMasterDataMapper MASTER_DATA_MAPPER = Mappers.getMapper(IMasterDataMapper.class);

    List<MasterDataTO> stateMedicalCouncilsToMasterDataTOs(List<StateMedicalCouncilTO> stateMedicalCouncils);

    List<MasterDataTO> countriesToMasterDataTOs(List<CountryTO> countries);

    List<MasterDataTO> statesToMasterDataTOs(List<StateTO> states);

    List<MasterDataTO> districtsToMasterDataTOs(List<DistrictTO> districts);

    List<MasterDataTO> subDistrictsToMasterDataTOs(List<SubDistrictTO> subDistricts);

    List<MasterDataTO> citiesToMasterDataTOs(List<VillagesTO> cities);

    List<MasterDataTO> specialitiesToMasterDataTOs(List<BroadSpecialityTO> specialities);

    List<MasterDataTO> universitiesToMasterDataTOs(List<UniversityTO> universities);

    List<MasterDataTO> collegesToMasterDataTOs(List<CollegeTO> colleges);

    List<MasterDataTO> languagesToMasterDataTOs(List<LanguageTO> languages);

    List<MasterDataTO> coursesToMasterDataTOs(List<CourseTO> courses);

    List<MasterDataTO> registrationRenewationTypeDataTOs(List<RegistrationRenewationTypeTO> registrationRenewationType);

    List<MasterDataTO> facilityTypeDataTOs(List<FacilityTypeTO> facilityType);

    List<CollegeMasterResponseTo> collegeMasterResponseDataTo(List<CollegeMasterTo> collegeMaster);

    List<UniversityMasterResponseTo> universityMasterResponseDataTo(List<UniversityMasterTo> universityMaster);

}
