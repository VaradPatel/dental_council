package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IStateMedicalCouncilMapper {

    List<StateMedicalCouncilTO> stateMedicalCouncilsToDtos(List<StateMedicalCouncil> stateMedicalCouncils);
}
