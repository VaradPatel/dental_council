package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IDashboardRepository extends JpaRepository<Dashboard, BigInteger> {

    @Query(value = "select * from main.dashboard d where request_id =:requestId ",nativeQuery = true)
    Dashboard findByRequestId(String requestId);
}
