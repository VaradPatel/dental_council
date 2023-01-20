package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, BigInteger> {

    @Query(value = "SELECT id,hp_profile_id,field_name,field_label,section_name,query_comment,common_comment,query_by,query_status,created_at,updated_at FROM queries WHERE hp_profile_id=:hpProfileId AND query_status='open'", nativeQuery = true)
    List<Queries> findQueriesByHpProfileId(BigInteger hpProfileId);

    Queries findQueriesById(BigInteger id);
}
