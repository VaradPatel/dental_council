package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import in.gov.abdm.nmr.mapper.IStatusWiseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
public interface IFetchCountOnCardRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_STATUS_WISE_COUNT_BY_GROUP_AND_APPLICATION_TYPE_QUERY,nativeQuery = true)
    List<IStatusWiseCount> fetchStatusWiseCountByGroupAndApplicationType(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId);

}
