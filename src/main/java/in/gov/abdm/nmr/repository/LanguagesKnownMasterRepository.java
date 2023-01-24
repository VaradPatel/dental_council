package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.LanguagesKnownMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigInteger;
import java.util.List;

public interface LanguagesKnownMasterRepository extends JpaRepository<LanguagesKnownMaster, BigInteger> {
	

	@Query(value = "select * from languages_known_master where hp_profile_id = :hpProfileId", nativeQuery = true)
	List<LanguagesKnownMaster> getLanguagesKnownByHpProfileId(BigInteger hpProfileId);
}
