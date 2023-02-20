package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

public interface ICollegeRepository extends JpaRepository<College, BigInteger> {


    String COLLEGE_REGISTRATION_QUERY = null;

    @Query(value = "SELECT * FROM colleges where university=:university", nativeQuery = true)
    List<College> getCollege(BigInteger university);

    @Query(value = "SELECT c FROM college c join c.user usr where usr.id=:userId")
    College findByUserId(BigInteger userId);

    College findCollegeById(BigInteger id);

    @Query(value = "SELECT * FROM colleges where request_id= :requestId", nativeQuery = true)
    College findCollegeByRequestId(String requestId);

    @Query(value = "SELECT c.id FROM college c WHERE c.user.id =:userId")
    List<BigInteger> getCollegeIdByUserId(@Param(USER_ID) BigInteger userId);

}
