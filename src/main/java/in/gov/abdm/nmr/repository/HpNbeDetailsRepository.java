package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface HpNbeDetailsRepository extends JpaRepository<HpNbeDetails, BigInteger> {

	@Query(value = "Select * from hp_nbe_details where request_id = :requestId ", nativeQuery = true)
	HpNbeDetails findByRequestId(String requestId);

	@Query(value = "Select * from hp_nbe_details where hp_profile_id = :hpProfileId ", nativeQuery = true)
	HpNbeDetails findByHpProfileId(BigInteger hpProfileId);

	@Query(value = "Select * from hp_nbe_details where user_id = :userId ", nativeQuery = true)
	HpNbeDetails findByUserId(BigInteger userId);
	
}
