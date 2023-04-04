package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

public interface WorkProfileRepository extends JpaRepository<WorkProfile, BigInteger> {
    @Query(value = FETCH_WORK_PROFILE_RECORDS_BY_HP_ID, nativeQuery = true)
    List<WorkProfile> getWorkProfileDetailsByHPId(@Param(HP_PROFILE_ID) BigInteger hpId);

    @Query(value = FETCH_WORK_PROFILE_RECORDS_BY_REG_NO, nativeQuery = true)
    List<WorkProfile> getWorkProfileDetailsByRegNo(@Param(REGISTRATION_NUMBER) String registrationNumber);

}
