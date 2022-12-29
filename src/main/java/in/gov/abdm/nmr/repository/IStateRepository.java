package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IStateRepository extends JpaRepository<State, BigInteger> {

    @Query(value = "SELECT * FROM state where country=:country", nativeQuery = true)
    List<State> getState(BigInteger country);
    

}
