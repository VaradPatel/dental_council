package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, BigInteger> {

    @Query(value = "SELECT course_name, id FROM course where system_of_medicine_id = 1 and visible_status = 1", nativeQuery = true)
    List<Course> getCourse();

    Course getByCourseName(String courseName);

}
