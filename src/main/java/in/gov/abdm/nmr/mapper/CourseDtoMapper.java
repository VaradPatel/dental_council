package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseDtoMapper {

    CourseDtoMapper COURSE_DTO_MAPPER = Mappers.getMapper(CourseDtoMapper.class);

    List<CourseTO> courseDataToDto(List<Course> course);

    CourseTO mapToCourseTO(Course course);

}
