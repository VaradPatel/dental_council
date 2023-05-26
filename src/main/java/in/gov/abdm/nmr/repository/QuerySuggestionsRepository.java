package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.QuerySuggestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface QuerySuggestionsRepository extends JpaRepository<QuerySuggestions, BigInteger> {

    @Query(value = "SELECT * from query_suggestions ORDER BY id ASC",nativeQuery = true)
    List<QuerySuggestions> findAll();

}
