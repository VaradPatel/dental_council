package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IStateMedicalCouncilMapper {

    List<StateMedicalCouncilTO> stateMedicalCouncilsToDtos(List<StateMedicalCouncil> stateMedicalCouncils);
}
