package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.CollegeDean;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDeanMapper {

    CollegeDean collegeDeanDtoToEntity(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
}
