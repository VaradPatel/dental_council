package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.NmrHprLinkage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface INmrHprLinkageRepository extends JpaRepository<NmrHprLinkage, BigInteger> {

	@Query(value = "select * from nmr_hpr_linkage where hp_profile_id=:hpProfileId",nativeQuery = true)
	NmrHprLinkage findByHpProfileId(BigInteger hpProfileId);
}
