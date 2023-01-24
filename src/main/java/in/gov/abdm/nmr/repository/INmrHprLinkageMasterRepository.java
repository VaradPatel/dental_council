package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.NmrHprLinkageMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface INmrHprLinkageMasterRepository extends JpaRepository<NmrHprLinkageMaster, BigInteger> {

	@Query(value = "select * from nmr_hpr_linkage_master where hp_profile_id=:hpProfileId",nativeQuery = true)
	NmrHprLinkageMaster findByHpProfileId(BigInteger hpProfileId);
}
