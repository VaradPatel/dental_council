package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeRepository extends JpaRepository<College, BigInteger> {

    @Query(value = "SELECT * FROM colleges where university=:university", nativeQuery = true)
    List<College> getCollege(BigInteger university);

    @Query(value = "SELECT c.* FROM colleges c inner join user_detail ud on c.user_detail = ud.id where ud.id=:userDetailId", nativeQuery = true)
    College findByUserDetail(Long userDetailId);
}
