package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.jpa.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseDtoMapper {

    List<CourseTO> courseDataToDto(List<Course> course);

    CourseTO mapToCourseTO(Course course);

}
