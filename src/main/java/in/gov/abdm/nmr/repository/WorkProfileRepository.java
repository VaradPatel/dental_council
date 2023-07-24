package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

public interface WorkProfileRepository extends JpaRepository<WorkProfile, BigInteger> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = DELINK_WORK_PROFILE_BY_FACILITY_ID)
    void delinkWorkProfileDetailsByFacilityId(@Param(USER_ID) BigInteger userId, @Param(FACILITY_ID) List<String> facilityId);

    @Query(value = FETCH_WORK_PROFILE_RECORDS_BY_USER_ID, nativeQuery = true)
    List<WorkProfile> getWorkProfileDetailsByUserId(@Param(USER_ID) BigInteger userId);
    @Query(value = FETCH_ACTIVE_WORK_PROFILE_RECORDS_BY_USER_ID, nativeQuery = true)
    List<WorkProfile> getActiveWorkProfileDetailsByUserId(@Param(USER_ID) BigInteger userId);

    @Transactional
    @Modifying
    @Query(value = "update work_profile set delete_status=true where user_id=:userId",nativeQuery = true)
    void markAsDeleteByHpUserId(BigInteger userId);

    @Transactional
    @Modifying
    @Query(value = "delete from work_profile where user_id=:userId and is_user_currently_working=1",nativeQuery = true)
    void deleteCurrentlyNotWorkingByHpUserId(BigInteger userId);

}
