package in.gov.abdm.nmr.api.controller.md.to;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpecialityTO;
import in.gov.abdm.nmr.db.sql.domain.college.CollegeTO;
import in.gov.abdm.nmr.db.sql.domain.country.CountryTO;
import in.gov.abdm.nmr.db.sql.domain.course.CourseTO;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;
import in.gov.abdm.nmr.db.sql.domain.facility_type.FacilityTypeTO;
import in.gov.abdm.nmr.db.sql.domain.language.LanguageTO;
import in.gov.abdm.nmr.db.sql.domain.registration_renewation_type.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.db.sql.domain.state.StateTO;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrictTO;
import in.gov.abdm.nmr.db.sql.domain.university.UniversityTO;
import in.gov.abdm.nmr.db.sql.domain.villages.VillagesTO;

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
