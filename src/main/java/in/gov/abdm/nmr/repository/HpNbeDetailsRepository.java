package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.HpProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HpNbeDetailsRepository extends JpaRepository<HpNbeDetails, BigInteger> {

	@Query(value = "Select * from hp_nbe_details where request_id = :requestId ", nativeQuery = true)
	HpNbeDetails findByRequestId(String requestId);

	@Query(value = "Select * from hp_nbe_details where hp_profile_id = :hpProfileId ", nativeQuery = true)
	HpNbeDetails findByHpProfileId(BigInteger hpProfileId);
	
}
