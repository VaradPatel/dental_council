package in.gov.abdm.nmr.repository;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.gov.abdm.nmr.entity.CollegeDean;

public interface ICollegeDeanRepository extends JpaRepository<CollegeDean, BigInteger> {

    @Query(value = "SELECT c FROM collegeDean c join c.user usr where usr.id=:userDetailId")
    CollegeDean findByUserDetail(BigInteger userDetailId);

    @Query(value = "SELECT cd.id FROM collegeDean cd WHERE cd.user.id =:userId")
    List<BigInteger> getCollegeDeanIdByUserId(@Param(USER_ID) BigInteger userId);
}
