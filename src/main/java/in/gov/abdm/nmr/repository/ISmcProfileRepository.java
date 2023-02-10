package in.gov.abdm.nmr.repository;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.gov.abdm.nmr.entity.SMCProfile;

public interface ISmcProfileRepository extends JpaRepository<SMCProfile, BigInteger> {

    @Query(value = "SELECT smc FROM smcProfile smc join smc.user usr where usr.id=:userDetailId")
    SMCProfile findByUserDetail(BigInteger userDetailId);

    @Query(value = "SELECT smc.stateMedicalCouncil.id FROM smcProfile smc WHERE smc.user.id =:userId")
    List<BigInteger> getSmcIdByUserId(@Param(USER_ID) BigInteger userId);
}
