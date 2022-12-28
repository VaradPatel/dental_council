package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeRegistrarRepository extends JpaRepository<CollegeRegistrar, BigInteger> {

    @Query(value = "SELECT cr.* FROM college_registrar cr inner join user_detail ud on cr.user_detail = ud.id where ud.id=:userDetailId", nativeQuery = true)
    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
