package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface ICollegeRepository extends JpaRepository<College, BigInteger> {


    String COLLEGE_REGISTRATION_QUERY = null;

    @Query(value = "SELECT * FROM colleges where university=:university", nativeQuery = true)
    List<College> getCollege(BigInteger university);

    @Query(value = "SELECT c FROM college c join c.user usr where usr.id=:userDetailId")
    College findByUserDetail(BigInteger userDetailId);

    College findCollegeById(BigInteger id);

}
