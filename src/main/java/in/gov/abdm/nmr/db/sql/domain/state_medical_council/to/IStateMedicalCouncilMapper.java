package in.gov.abdm.nmr.db.sql.domain.state_medical_council.to;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IStateMedicalCouncilMapper {

    List<StateMedicalCouncilTO> stateMedicalCouncilsToDtos(List<StateMedicalCouncil> stateMedicalCouncils);
}
