package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface WorkProfileMasterRepository extends JpaRepository<WorkProfileMaster, BigInteger> {

    @Query(value = "SELECT * FROM work_profile_master where hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkProfileMaster getWorkProfileByHpProfileId(BigInteger hpProfileId);
}
