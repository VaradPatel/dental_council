package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseDtoMapper {

    List<CourseTO> CourseDataToDto(List<Course> course);


}
