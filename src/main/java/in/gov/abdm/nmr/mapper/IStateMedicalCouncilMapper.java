package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IStateMedicalCouncilMapper {
    IStateMedicalCouncilMapper STATE_MEDICAL_COUNCIL_MAPPER = Mappers.getMapper(IStateMedicalCouncilMapper.class);

    List<StateMedicalCouncilTO> stateMedicalCouncilsToDtos(List<StateMedicalCouncil> stateMedicalCouncils);
}
