package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeDean;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDeanMapper {

    CollegeDean collegeDeanDtoToEntity(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
}
