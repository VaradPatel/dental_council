package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigInteger;
import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, BigInteger> {

    List<Queries> findQueriesByHpProfileId(Long hpProfileId);

    Queries findQueriesById(Long id);
}
