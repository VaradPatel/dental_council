package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeRegistrarMapper {

    CollegeRegistrar collegeRegistrarDtoToEntity(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
}
