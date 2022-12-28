package in.gov.abdm.nmr.db.sql.domain.nmc_profile;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INmcProfileRepository extends JpaRepository<NmcProfile, BigInteger>{
    
    @Query(value = "SELECT nmc.* FROM nmc_profile nmc inner join user_detail ud on nmc.user_detail = ud.id where ud.id=:userDetailId", nativeQuery = true)
    NmcProfile findByUserDetail(BigInteger userDetailId);
}
