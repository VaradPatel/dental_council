package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.NmcProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INmcProfileRepository extends JpaRepository<NmcProfile, BigInteger>{
    
    @Query(value = "SELECT nmc.* FROM nmc_profile nmc inner join user ud on nmc.user_id = ud.id where ud.id=:userDetailId", nativeQuery = true)
    NmcProfile findByUserDetail(BigInteger userDetailId);
}
