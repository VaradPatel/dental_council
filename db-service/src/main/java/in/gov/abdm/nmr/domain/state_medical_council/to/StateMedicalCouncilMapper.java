package in.gov.abdm.nmr.domain.state_medical_council.to;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.domain.state_medical_council.StateMedicalCouncil;

@Mapper(componentModel = ComponentModel.SPRING)
public interface StateMedicalCouncilMapper {

    StateMedicalCouncilTO stateMedicalCouncilToDto(StateMedicalCouncil stateMedicalCouncil);
}
