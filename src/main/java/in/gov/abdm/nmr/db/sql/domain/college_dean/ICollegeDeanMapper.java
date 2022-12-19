package in.gov.abdm.nmr.db.sql.domain.college_dean;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDeanMapper {

    CollegeDean collegeDeanDtoToEntity(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
}
