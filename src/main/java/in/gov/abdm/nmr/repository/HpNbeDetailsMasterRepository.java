package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpNbeDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface HpNbeDetailsMasterRepository extends JpaRepository<HpNbeDetailsMaster, BigInteger> {

	@Query(value = "Select * from hp_nbe_details_master where request_id = :requestId ", nativeQuery = true)
	HpNbeDetailsMaster findByRequestId(String requestId);

	@Query(value = "Select * from hp_nbe_details_master where hp_profile_id = :hpProfileId ", nativeQuery = true)
	HpNbeDetailsMaster findByHpProfileId(BigInteger hpProfileId);
	
}
