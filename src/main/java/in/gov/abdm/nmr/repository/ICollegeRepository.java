package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeRepository extends JpaRepository<College, BigInteger> {

    @Query(value = "SELECT * FROM colleges where university=:university", nativeQuery = true)
    List<College> getCollege(BigInteger university);

    @Query(value = "SELECT c FROM college c join c.user usr where usr.id=:userDetailId")
    College findByUserDetail(BigInteger userDetailId);
}