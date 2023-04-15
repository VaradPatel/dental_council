package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.mapper.CourseDtoMapper;
import in.gov.abdm.nmr.repository.CourseRepository;
import in.gov.abdm.nmr.service.ICourseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements ICourseService {

    private CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseTO> getCourseData() {
        return CourseDtoMapper.COURSE_DTO_MAPPER.courseDataToDto(courseRepository.getCourse());
    }
}
