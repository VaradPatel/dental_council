package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.SMCProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface ISmcProfileRepository extends JpaRepository<SMCProfile, BigInteger> {

    @Query(value = "SELECT smc FROM smcProfile smc join smc.user usr where usr.id=:userDetailId")
    SMCProfile findByUserDetail(BigInteger userDetailId);
}
