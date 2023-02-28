package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeRegistrarMapper {

    CollegeRegistrar collegeRegistrarDtoToEntity(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
}
