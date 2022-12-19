package in.gov.abdm.nmr.db.sql.domain.work_profile;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkProfileRepository extends JpaRepository<WorkProfile, BigInteger> {

    @Query(value = "SELECT * FROM work_profile where hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkProfile getWorkProfileByHpProfileId(BigInteger hpProfileId);

}
