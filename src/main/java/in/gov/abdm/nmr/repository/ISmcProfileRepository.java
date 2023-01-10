package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.gov.abdm.nmr.entity.SMCProfile;

public interface ISmcProfileRepository extends JpaRepository<SMCProfile, BigInteger> {

    @Query(value = "SELECT smc FROM smcProfile smc join smc.user usr where usr.id=:userDetailId")
    SMCProfile findByUserDetail(BigInteger userDetailId);
}
