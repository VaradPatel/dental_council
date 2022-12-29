package in.gov.abdm.nmr.db.sql.domain.course;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, BigInteger> {

    @Query(value = "SELECT course_name as name, id FROM course", nativeQuery = true)
    List<Course> getCourse();

}
