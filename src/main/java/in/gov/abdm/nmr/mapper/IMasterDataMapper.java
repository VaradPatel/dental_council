package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.MasterDataTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.dto.CollegeTO;
import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.dto.VillagesTO;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IMasterDataMapper {

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

}
