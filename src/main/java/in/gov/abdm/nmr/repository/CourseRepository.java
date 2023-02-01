package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, BigInteger> {

    @Query(value = "SELECT course_name, id FROM course", nativeQuery = true)
    List<Course> getCourse();

}
