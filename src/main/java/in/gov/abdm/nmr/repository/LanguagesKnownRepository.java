package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.LanguagesKnown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface LanguagesKnownRepository extends JpaRepository<LanguagesKnown, BigInteger> {


	List<LanguagesKnown> findByUserId(BigInteger userId);

	@Transactional
	@Modifying
	@Query(value = "delete from languages_known where user_id=:userId",nativeQuery = true)
	void deleteAllByHpUserId(BigInteger userId);
}
