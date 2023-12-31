package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IStateRepository extends JpaRepository<State, BigInteger> {

    @Query(value = "SELECT id, iso_code, INITCAP(name) name ,country, created_at ,updated_at FROM state where country=:country order by name asc", nativeQuery = true)
    List<State> getState(BigInteger country);

    @Query(value = "SELECT * FROM state WHERE name ILIKE :name", nativeQuery = true)
    State findByName(String name);

    @Query(value = "SELECT * FROM state WHERE iso_code= :stateIsoCode", nativeQuery = true)
    State getStateByIsoCode(String stateIsoCode);
}
