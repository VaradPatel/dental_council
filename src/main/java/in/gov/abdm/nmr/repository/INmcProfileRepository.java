package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.NmcProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface INmcProfileRepository extends JpaRepository<NmcProfile, BigInteger> {
    @Query(value = "SELECT nmc FROM nmcProfile nmc join nmc.user usr where usr.id=:userDetailId")
    NmcProfile findByUserDetail(BigInteger userDetailId);
}
