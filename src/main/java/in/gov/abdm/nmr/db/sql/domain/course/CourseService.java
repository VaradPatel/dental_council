package in.gov.abdm.nmr.db.sql.domain.course;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class CourseService implements ICourseService {

    public CourseRepository courseRepository;

    private CourseDtoMapper courseDtoMapper;

    public CourseService( CourseRepository courseRepository, CourseDtoMapper courseDtoMapper) {
        this.courseRepository = courseRepository;
        this.courseDtoMapper = courseDtoMapper;
    }

    @Override
    public List<CourseTO> getCourseData() {
        return courseDtoMapper.CourseDataToDto(courseRepository.getCourse());

    }
}
