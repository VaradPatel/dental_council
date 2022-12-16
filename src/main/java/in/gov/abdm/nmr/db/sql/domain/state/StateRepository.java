package in.gov.abdm.nmr.db.sql.domain.state;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StateRepository extends JpaRepository<State, BigInteger> {

    @Query(value = "SELECT * FROM state where country=:country", nativeQuery = true)
    List<State> getState(BigInteger country);
    

}
