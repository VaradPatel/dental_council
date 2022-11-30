package in.gov.abdm.nmr.domain.state;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StateRepository extends JpaRepository<State, BigInteger> {

	@Query(value = "SELECT name, id FROM state where country=:country", nativeQuery = true)
	List<Tuple> getState(BigInteger country);

}
