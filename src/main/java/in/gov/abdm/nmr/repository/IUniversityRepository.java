package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IUniversityRepository extends JpaRepository<University, BigInteger> {

    @Query(value = "SELECT name, id, location FROM universities", nativeQuery = true)
    List<University> getUniversity();

}
