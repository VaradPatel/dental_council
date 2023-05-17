package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.LanguagesKnownMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface LanguagesKnownMasterRepository extends JpaRepository<LanguagesKnownMaster, BigInteger> {


	List<LanguagesKnownMaster> findByUserId(BigInteger userId);
}
