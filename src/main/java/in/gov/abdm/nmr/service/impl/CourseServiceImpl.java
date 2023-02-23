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

    public CourseRepository courseRepository;

    private CourseDtoMapper courseDtoMapper;

    public CourseServiceImpl(CourseRepository courseRepository, CourseDtoMapper courseDtoMapper) {
        this.courseRepository = courseRepository;
        this.courseDtoMapper = courseDtoMapper;
    }

    @Override
    public List<CourseTO> getCourseData() {
        return courseDtoMapper.CourseDataToDto(courseRepository.getCourse());

    }
}
