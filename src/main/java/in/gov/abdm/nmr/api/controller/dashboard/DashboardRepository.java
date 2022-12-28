package in.gov.abdm.nmr.api.controller.dashboard;

import static in.gov.abdm.nmr.api.constant.NMRConstants.IS_NEW;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.api.controller.dashboard.to.StatusWiseCount;
import in.gov.abdm.nmr.db.sql.domain.hp_verification_status.HpVerificationStatus;

@Repository
public interface DashboardRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value ="SELECT a.applicationStatusType.name as name, COUNT(a) as count " +
            "FROM HpVerificationStatus a " +
            "WHERE a.isNewApplication=:isNew " +
            "GROUP BY a.applicationStatusType.name")
    List<StatusWiseCount> fetchCountOnCard(@Param(IS_NEW) Integer isNew);

    List<HpVerificationStatus> findByIsNewApplication(Integer isNewApplication);


}
