package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseDtoMapper {

    List<CourseTO> CourseDataToDto(List<Course> course);


}
