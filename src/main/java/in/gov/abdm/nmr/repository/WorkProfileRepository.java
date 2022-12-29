package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.WorkProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkProfileRepository extends JpaRepository<WorkProfile, BigInteger> {

    @Query(value = "SELECT * FROM work_profile where hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkProfile getWorkProfileByHpProfileId(BigInteger hpProfileId);

}
