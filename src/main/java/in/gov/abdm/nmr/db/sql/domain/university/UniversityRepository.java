package in.gov.abdm.nmr.db.sql.domain.university;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query(value = "SELECT name, id, location FROM universities", nativeQuery = true)
    List<University> getUniversity();

}
