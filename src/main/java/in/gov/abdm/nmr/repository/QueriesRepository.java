package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, BigInteger> {

    @Query(value = "SELECT q FROM queries q WHERE q.hp_profile_id=:hpProfileId AND q.query_status='open'", nativeQuery = true)
    List<Queries> findQueriesByHpProfileId(BigInteger hpProfileId);

    Queries findQueriesById(BigInteger id);
}
