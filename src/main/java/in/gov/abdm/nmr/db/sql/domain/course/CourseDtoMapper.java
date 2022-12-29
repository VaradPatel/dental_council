package in.gov.abdm.nmr.db.sql.domain.course;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseDtoMapper {

    List<CourseTO> CourseDataToDto(List<Course> course); 


}
