package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import in.gov.abdm.nmr.entity.WorkProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkProfileMasterRepository extends JpaRepository<WorkProfileMaster, BigInteger> {

    @Query(value = "SELECT * FROM work_profile_master where hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkProfileMaster getWorkProfileByHpProfileId(BigInteger hpProfileId);
}
