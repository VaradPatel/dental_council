package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeRegistrarMapper {

    CollegeRegistrar collegeRegistrarDtoToEntity(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
}
