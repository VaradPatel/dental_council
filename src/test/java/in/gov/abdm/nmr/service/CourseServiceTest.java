package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.repository.CourseRepository;
import in.gov.abdm.nmr.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void testGetCountryDataShouldReturnValidListOfCountries(){
        when(courseRepository.getCourse()).thenReturn(List.of(getCourse()));
        List<CourseTO> courses = courseService.getCourseData();
        assertTrue(courses.size() == 1);
        CourseTO courseTO = courses.get(0);
        assertEquals(ID, courseTO.getId());
        assertEquals(COURSE_NAME, courseTO.getCourseName());
    }
}