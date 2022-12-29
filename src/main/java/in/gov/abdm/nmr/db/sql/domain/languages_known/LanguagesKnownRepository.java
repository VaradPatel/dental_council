package in.gov.abdm.nmr.db.sql.domain.languages_known;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LanguagesKnownRepository extends JpaRepository<LanguagesKnown, BigInteger> {
	

	@Query(value = "select * from languages_known where hp_profile_id = :hpProfileId", nativeQuery = true)
	List<LanguagesKnown> getLanguagesKnownByHpProfileId(BigInteger hpProfileId);
}
