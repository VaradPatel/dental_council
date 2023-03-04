package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_WORK_PROFILE_RECORDS_BY_HP_ID;
import static in.gov.abdm.nmr.util.NMRConstants.HP_PROFILE_ID;

public interface WorkProfileRepository extends JpaRepository<WorkProfile, BigInteger> {
    @Query(value = FETCH_WORK_PROFILE_RECORDS_BY_HP_ID, nativeQuery = true)
    List<WorkProfile> getWorkProfileDetailsByHPId(@Param(HP_PROFILE_ID) BigInteger hpId);

}
